package cn.ymotel.dpress.admin.journals;

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
import run.halo.app.model.support.CommentPage;

import java.util.*;

@ActorCfg(urlPatterns = "/api/admin/journals/comments/{postid}/tree_view",methods = RequestMethod.GET)
public class CommentTreeViewActor implements Actor<ServletMessage> {

    @Autowired
    private SqlSession sqlSession;
    @Override
    public Page Execute(ServletMessage message) throws Throwable {
        int ipage=message.getContextData("page",0);
        int isize=message.getContextData("size",10);
        Pageable pageable= PageRequest
                .of(ipage, isize);
        Map map = new HashMap();
        map.put("siteid", Utils.getSiteId());
        map.put("type", 2);
        map.put("post_id",message.getContextData("postid"));
        List ls= sqlSession.selectList("comments.qallbypostid",map);
       List treeList= buildTree(ls,"id","parentId",0);
       List pageContent=null;
        int startIndex = pageable.getPageNumber() * pageable.getPageSize();
        if (startIndex >= treeList.size() || startIndex < 0) {
            pageContent = Collections.emptyList();
        } else {
            int endIndex = startIndex + pageable.getPageSize();
            if (endIndex > treeList.size()) {
                endIndex = treeList.size();
            }
            pageContent = treeList.subList(startIndex, endIndex);
        }

        CommentPage page=new CommentPage(pageContent,pageable,treeList.size(),ls.size());


        return page;

    }
    public List buildTree(List<Map> list,String idKey,String parentIdKey,Object parentId){
        List rtnList=new ArrayList();
        for(Map map:list){
            Object tparentId=map.get(parentIdKey);
            Object id=map.get(idKey);
            if(tparentId==null){
                continue;
            }
            if(tparentId.toString().equals(parentId.toString())){
                rtnList.add(map);
            }else{
                continue;
            }
            List list2=buildTree(list,idKey,parentIdKey,id);
            if(list2==null||list2.size()==0){
                map.put("children", null);

            }else {
                map.put("children", list2);
            }
        }
        return rtnList;
    }
}
