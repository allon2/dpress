package cn.ymotel.dpress.actor;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.service.OptionsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

@ActorCfg(urlPatterns = {"/favicon.ico"},chain = "publicchain")
public class FaviconActor  implements Actor<ServletMessage> {
    @Autowired
    private OptionsService optionsService;

    @Override
    public String Execute(ServletMessage message) throws Throwable {

        String favicon =optionsService.getOption(Utils.getFrontSiteId(message.getRequest()),"blog_favicon",null);
        if (StringUtils.isNotEmpty(favicon)) {
            return "redirect:"+favicon;
        }
        return null;
    }
}
