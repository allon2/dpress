package cn.ymotel.dpress.admin.sitemgmt;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;

import java.util.HashMap;
@ActorCfg(urlPatterns = "/api/admin/site/changesessionsite.json")
public class ChangeCurrentSiteActor implements Actor<ServletMessage> {
    public static String SESSION_SITE="_site";
    @Override
    public Object Execute(ServletMessage message) throws Throwable {
        System.out.println("messageData"+message.getContext());
        message.getRequest().getSession().setAttribute(SESSION_SITE,message.getContextData("id")+"");
        return new HashMap<>();
    }
}
