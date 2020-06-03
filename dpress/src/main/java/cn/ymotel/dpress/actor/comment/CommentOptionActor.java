package cn.ymotel.dpress.actor.comment;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.service.OptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/content/options/comment",chain = "publicchain",methods = RequestMethod.GET)
public class CommentOptionActor implements Actor<ServletMessage> {
    @Autowired
    OptionsService optionsService;

    @Override
    public Map Execute(ServletMessage message) throws Throwable {
        Map map=new HashMap<>();
        map.put(optionsService.GRAVATAR_DEFAULT,optionsService.getGRAVATAR_DEFAULT(Utils.getSiteId()));
        map.put(optionsService.CONTENT_PLACEHOLDER,optionsService.getCONTENT_PLACEHOLDER(Utils.getSiteId()));
        return map;
    }
}
