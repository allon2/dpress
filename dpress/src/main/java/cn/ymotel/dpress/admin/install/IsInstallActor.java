package cn.ymotel.dpress.admin.install;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;

@ActorCfg(urlPatterns = "/api/admin/is_installed",chain = "publicchain")
public class IsInstallActor implements Actor<ServletMessage> {
    @Override
    public Boolean Execute(ServletMessage message) throws Throwable {
        return  Utils.isInstall();
    }
}
