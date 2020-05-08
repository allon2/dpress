package cn.ymotel.dpress.admin.postcomments;

import cn.hutool.core.util.URLUtil;
import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.service.OptionsService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMethod;
import run.halo.app.model.enums.CommentStatus;

import java.util.HashMap;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/posts/comments",methods = RequestMethod.POST)
public class ReplyCommentActor implements Actor<ServletMessage> {
    @Autowired
    OptionsService optionsService;
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Map Execute(ServletMessage message) throws Throwable {
        run.halo.app.model.entity.User user=(run.halo.app.model.entity.User)message.getUser();
        Map params=new HashMap();
        params.put("create_time",new java.sql.Timestamp(System.currentTimeMillis()));
        params.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
        params.put("type",0);
        params.put("email",user.getEmail());
        params.put("author", StringUtils.isBlank(user.getNickname()) ? user.getUsername() : user.getNickname());
        String author_url=optionsService.getOption(Utils.getSiteId(),"blog_url",null);
        if(StringUtils.isNotEmpty(author_url)){
            author_url= URLUtil.normalize(author_url);
        }
        params.put("author_url",author_url);
        if(user!=null) {
            params.put("is_admin", true);
        }
        params.put("status", CommentStatus.PUBLISHED.getValue());
        params.put("siteid", Utils.getSiteId());
        params.put("post_id",message.getContextData("postId"));
        params.put("parent_id",message.getContextData("parentId"));
        params.put("content",message.getContextData("content"));
        params.put("ip_address",message.getClientIp());
        params.put("user_agent",message.getHeader(HttpHeaders.USER_AGENT));
        params.put("allow_notification",true);
        if(params.get("email")!=null){
            params.put("gravatar_md5", DigestUtils.md5Hex((String)params.get("email")));
        }
        sqlSession.insert("comments.i",params);
        return new HashMap();
    }
}
