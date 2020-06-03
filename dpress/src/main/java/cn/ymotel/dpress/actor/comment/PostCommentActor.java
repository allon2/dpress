package cn.ymotel.dpress.actor.comment;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.service.OptionsService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMethod;
import run.halo.app.model.enums.CommentStatus;

import java.util.HashMap;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/content/posts/comments",chain = "publicchain",methods = RequestMethod.POST)
public class PostCommentActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Autowired
    OptionsService optionsService;
    @Override
    public Map Execute(ServletMessage message) throws Throwable {
         Map params=new HashMap();
        params.put("create_time",new java.sql.Timestamp(System.currentTimeMillis()));
        params.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
        params.put("type",0);
        params.put("email",message.getContextData("email"));
        params.put("author",message.getContextData("author"));
        params.put("siteid", Utils.getSiteId());
        if(optionsService.getNEW_NEED_CHECK(Utils.getSiteId())) {
            params.put("status", CommentStatus.AUDITING.getValue());
        }else{
            params.put("status", CommentStatus.PUBLISHED.getValue());

        }
        params.put("post_id",message.getContextData("postId"));
        params.put("ip_address",message.getClientIp());
        params.put("user_agent",message.getHeader(HttpHeaders.USER_AGENT));
        params.put("allow_notification",true);
        if(params.get("email")!=null){
            params.put("gravatar_md5", DigestUtils.md5Hex((String)params.get("email")));
        }
        if(message.getContextData("parentId")==null) {
            params.put("parent_id", 0);
        }else{
            params.put("parent_id", message.getContextData("parentId"));

        }
        params.put("content",message.getContextData("content"));
        sqlSession.insert("comments.i",params);
        if(optionsService.getNEW_NEED_CHECK(Utils.getSiteId())) {
            params.put("status", CommentStatus.AUDITING.name());
        }else{
            params.put("status", CommentStatus.PUBLISHED.name());

        }
        return params;
    }
}
