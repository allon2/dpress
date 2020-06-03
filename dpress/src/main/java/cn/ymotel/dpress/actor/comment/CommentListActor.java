package cn.ymotel.dpress.actor.comment;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.service.OptionsService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMethod;
import run.halo.app.model.enums.CommentStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/content/posts/{postid}/comments/top_view",chain = "publicchain",methods = RequestMethod.GET)
public class CommentListActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Autowired
    OptionsService optionsService;
    @Override
    public PageImpl Execute(ServletMessage message) throws Throwable {
        Map params=new HashMap();
        params.put("siteid",Utils.getSiteId());
        params.put("status",CommentStatus.PUBLISHED.getValue());
        params.put("parent_id",0L);
        params.put("post_id",message.getContextData("postid"));
        PageRequest page=PageRequest.of(message.getContextData("page",0),message.getContextData("size",0));
        params.put("start",page.getOffset());
        params.put("size",page.getPageSize());
       List list= sqlSession.selectList("comments.qlimit",params);
       for(int i=0;i<list.size();i++){
           Map tMap=(Map)list.get(i);
           Map param1=new HashMap();
           param1.put("siteid",Utils.getSiteId());
           param1.put("post_id",message.getContextData("postid"));
           param1.put("parent_id",tMap.get("id"));

          long count= sqlSession.selectOne("comments.qcountbypostidAndparentId",param1);
           if(count>0){
               tMap.put("hasChildren",true);
           }else{
               tMap.put("hasChildren",false);

           }
       }
        PageImpl pageImpl=new PageImpl(list,page,sqlSession.selectOne("comments.qcount"));
       System.out.println(list);
        return pageImpl;
    }
}
