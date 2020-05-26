package cn.ymotel.dpress.admin.users;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.Message;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.service.OptionsService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/users/profiles",methods = RequestMethod.PUT)
public class UserProfileSaveActor implements Actor<Message> {
    @Autowired
    private SqlSession sqlSession;


    @Override
    public Map Execute(Message message) throws Throwable {
        Map rtnMap=message.getContext();
        rtnMap.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
        sqlSession.update("users.u",rtnMap);
        return rtnMap;
    }
}
