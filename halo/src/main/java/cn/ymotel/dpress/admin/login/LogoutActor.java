package cn.ymotel.dpress.admin.login;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.Message;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.service.AutokenService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import run.halo.app.model.entity.User;
import run.halo.app.security.token.AuthToken;

import java.util.HashMap;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/logout",methods = RequestMethod.POST)
public class LogoutActor implements Actor<ServletMessage> {

    @Autowired
    AutokenService autokenService;


    @Override
    public Map Execute(ServletMessage message) throws Throwable {

        autokenService.clearToken((User)message.getUser());
        message.getRequest().getSession().invalidate();
        return new HashMap();
    }

}
