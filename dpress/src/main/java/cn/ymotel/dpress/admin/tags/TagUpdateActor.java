package cn.ymotel.dpress.admin.tags;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/tags/{id}",methods = RequestMethod.PUT)
public class TagUpdateActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Object Execute(ServletMessage message) throws Throwable {
        Map map=message.getContext();
        map.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
        map.put("siteid", Utils.getSiteId());
        sqlSession.update("tags.u",map);
        return new HashMap<>();
    }
}
