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

@ActorCfg(urlPatterns = "/api/admin/posts/{id}",methods = RequestMethod.GET)
public class PostOneActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Map Execute(ServletMessage message) throws Throwable {
         Map params=message.getContext();
         params.put("siteid",Utils.getSiteId());
         Map rtnMap=sqlSession.selectOne("posts.q",params);

           Integer status=(Integer)rtnMap.get("status");
        rtnMap.put("status",ValueEnum.valueToEnum(PostStatus.class,status).name());
        rtnMap.put("commentCount",getCommentCount(Utils.getSiteId(),rtnMap.get("id")));
        rtnMap.put("metas",getMeta(Utils.getSiteId(),rtnMap.get("id")));
        rtnMap.put("categories",getPostCategories(Utils.getSiteId(),rtnMap.get("id")));
        rtnMap.put("tags",getPostTags(Utils.getSiteId(),rtnMap.get("id")));
        rtnMap.put("disallowComment",rtnMap.get("disallow_comment"));
        rtnMap.put("topPriority",rtnMap.get("top_priority"));
        rtnMap.put("editTime",rtnMap.get("edit_time"));
        rtnMap.put("formatContent",rtnMap.get("format_content"));
        rtnMap.put("originalContent",rtnMap.get("original_content"));

        return rtnMap;
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
            Object obj=sqlSession.selectOne("categories.qdetailbypostid",ttMap);
            if(obj!=null) {
                rtnList.add(obj);
            }
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
