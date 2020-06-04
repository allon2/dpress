package cn.ymotel.dpress.admin.theme;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;

import java.util.HashSet;
import java.util.Set;

@ActorCfg(urlPatterns = "/api/admin/themes/activation/template/custom/sheet")
public class CustomSheetTemplateActor implements Actor<ServletMessage> {
    @Override
    public Set Execute(ServletMessage message) throws Throwable {
        return new HashSet();
    }
}
