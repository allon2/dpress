package cn.ymotel.dpress.admin.posts;


import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMethod;
import run.halo.app.model.enums.PostStatus;
import run.halo.app.model.enums.ValueEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//@ActorCfg(urlPatterns = "/api/admin/posts")

@ActorCfg(urlPatterns = "/api/admin/posts",methods = RequestMethod.GET)
public class PostListActor  implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Page Execute(ServletMessage message) throws Throwable {
        String currentPage=message.getContextData("page","0");
        int ipage=Integer.parseInt(currentPage);
        String size=message.getContextData("size","10");
        int isize=Integer.parseInt(size);
        Pageable pageable= PageRequest
                .of(ipage>= 1 ? ipage - 1 : ipage, isize);
        Map map = new HashMap();
        map.put("siteid", Utils.getSiteIdFromMessage(message));
        map.put("start", pageable.getOffset());
        map.put("size",isize);
        if(message.getContextData("status")!=null) {
            map.put("status", PostStatus.valueOf(message.getContextData("status")).getValue());
        }
        if(message.getContextData("keyword")!=null) {
            map.put("keyword1", message.getContextData("keyword"));
            map.put("keyword", "%" + message.getContextData("keyword") + "%");
        }
        map.put("categoryId",message.getContextData("categoryId"));
       List ls= sqlSession.selectList("posts.qadminposts",map);
       for(int i=0;i<ls.size();i++){
           Map tMap=(Map)ls.get(i);
           Integer status=(Integer)tMap.get("status");
           tMap.put("status",ValueEnum.valueToEnum(PostStatus.class,status).name());
           tMap.put("commentCount",getCommentCount(Utils.getSiteId(),tMap.get("id")));
           tMap.put("metas",getMeta(Utils.getSiteId(),tMap.get("id")));
           tMap.put("categories",getPostCategories(Utils.getSiteId(),tMap.get("id")));
           tMap.put("tags",getPostTags(Utils.getSiteId(),tMap.get("id")));
       }
        Map ctMap= sqlSession.selectOne("posts.qadmincountposts",map);
        long total=Long.parseLong(ctMap.get("ct").toString());
        Page page=new PageImpl(ls,pageable,total);

        return page;
    }
    public List getPostTags(Object siteid,Object postid){
        Map tMap=new HashMap();
        tMap.put("siteid",siteid);
        tMap.put("post_id",postid);
       return sqlSession.selectList("posttag.qpostbypostid",tMap);
    }
    public List getPostCategories(Object siteid,Object postid){
        Map tMap=new HashMap();
        tMap.put("siteid",siteid);
        tMap.put("postid",postid);
        List list= sqlSession.selectList("categories.qbypostid",tMap);
        List rtnList=new ArrayList();
        for(int i=0;i<list.size();i++){
            Map tListMap=(Map)list.get(i);
            Map ttMap=new HashMap();
            ttMap.put("siteid",siteid);
            ttMap.put("id",tListMap.get("category_id"));
            rtnList.add(sqlSession.selectOne("categories.qdetailbypostid",ttMap));
        }
        return rtnList;
    }
    public Long getCommentCount(Object siteid,Object postid){
        Map tMap=new HashMap();
        tMap.put("siteid",siteid);
        tMap.put("post_id",postid);
        return sqlSession.selectOne("comments.qcountbypostid",tMap);

    }
    public Map getMeta(Object siteid,Object postid){
        Map tMap=new HashMap();
        tMap.put("siteid",siteid);
        tMap.put("post_id",postid);
        tMap.put("type",0);
        List ls= sqlSession.selectList("meta.qbypostid",tMap);
        Map rtnMap=new HashMap();
        for(int i=0;i<ls.size();i++){
            Map ttMap=(Map)ls.get(i);
            rtnMap.put(ttMap.get("meta_key"),ttMap.get("meta_value"));
        }
        return rtnMap;
    }
    public static void main(String[] args){
        System.out.println(PostStatus.valueOf("PUBLISHED").getValue());
    }
}
