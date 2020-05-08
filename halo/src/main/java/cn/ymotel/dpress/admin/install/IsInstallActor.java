package cn.ymotel.dpress.admin.install;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;

@ActorCfg(urlPatterns = "/api/admin/is_installed")
public class IsInstallActor implements Actor<ServletMessage> {
    @Override
    public Boolean Execute(ServletMessage message) throws Throwable {
        System.out.println(Utils.isInstall());
        return  Utils.isInstall();
    }
}
