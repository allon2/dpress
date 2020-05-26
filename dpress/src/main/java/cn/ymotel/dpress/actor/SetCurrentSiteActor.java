package cn.ymotel.dpress.actor;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;

import java.util.HashMap;

@ActorCfg(urlPatterns = "/admin/currentsiteset")

public class SetCurrentSiteActor implements Actor<ServletMessage> {
    private static String SESSION_SITEID="_siteid";
    @Override
    public Object Execute(ServletMessage message) throws Throwable {
        message.getRequest().getSession().setAttribute(SESSION_SITEID,message.getControlData("siteid"));
        return new HashMap();
    }
}
