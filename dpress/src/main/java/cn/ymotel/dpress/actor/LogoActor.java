package cn.ymotel.dpress.actor;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.service.OptionsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import run.halo.app.model.properties.BlogProperties;

@ActorCfg(urlPatterns = {"/logo"},chain = "publicchain")
public class LogoActor implements Actor<ServletMessage> {
    @Autowired
    private OptionsService optionsService;

    @Override
    public String Execute(ServletMessage message) throws Throwable {
        String blogLogo = optionsService.getOption(Utils.getFrontSiteId(message.getRequest()),"blog_logo",null);
        if (StringUtils.isNotEmpty(blogLogo)) {
            return "redirect:"+blogLogo;
        }
        return null;
    }


}
