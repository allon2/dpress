package cn.ymotel.dpress.admin.journals;

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

@ActorCfg(urlPatterns = "/api/admin/journals/comments/{id}/status/{status}",methods = RequestMethod.PUT)
public class CommentStatusActor implements Actor<ServletMessage> {
   
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Map Execute(ServletMessage message) throws Throwable {
        Map map=new HashMap<>();
        map.put("siteid", Utils.getSiteId());
        map.put("id",message.getContextData("id"));
        map.put("status", CommentStatus.valueOf(message.getContextData("status")).getValue());
        sqlSession.update("comments.ustatus",map);

        return new HashMap();
    }
}
