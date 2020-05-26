package cn.ymotel.dpress.admin.login;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.Message;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/login/precheck",methods = RequestMethod.POST)
public class LoginPreCheckActor implements Actor<Message> {
    @Override
    public Map Execute(Message message) throws Throwable {
        Map map=new HashMap();
        map.put("needMFACode",false);
        return map;

    }
}
