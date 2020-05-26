package cn.ymotel.dpress.admin.postcomments;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import run.halo.app.model.enums.CommentStatus;

import java.util.HashMap;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/posts/comments/{commentId:\\d+}",methods = RequestMethod.DELETE)
public class DeleteCommentActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;

    @Override
    public Map Execute(ServletMessage message) throws Throwable {
        Map map=new HashMap<>();
        map.put("siteid", Utils.getSiteId());
        map.put("id",message.getContextData("commentId"));
        sqlSession.update("comments.d",map);
        return new HashMap<>();
    }
}
