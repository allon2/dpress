package cn.ymotel.dpress.actor;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.Message;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.service.OptionsService;
import org.springframework.beans.factory.annotation.Autowired;

@ActorCfg(urlPatterns = "/ads.txt",chain = "publicchain")
public class AdsActor  implements Actor<ServletMessage> {
    @Autowired
    private  OptionsService optionsService;
    @Override
    public Object HandleMessage(ServletMessage message) throws Throwable {
        message.getResponse().setContentType("text/plain; charset=utf-8");
        message.getResponse().setCharacterEncoding("UTF-8");
        message.getResponse().getWriter().println(optionsService.getOption(Utils.getSiteId(),"googleadsense_ads",""));
        message.getResponse().getWriter().flush();
        message.getAsyncContext().complete();
        return null;
    }
}
