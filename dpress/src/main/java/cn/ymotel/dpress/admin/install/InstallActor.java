package cn.ymotel.dpress.admin.install;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import run.halo.app.config.properties.HaloProperties;

@ActorCfg(urlPatterns = "/install",chain = "publicchain")
public class InstallActor implements Actor<ServletMessage> {
    @Autowired
    private  HaloProperties haloProperties;

    @Override
    public <E> E HandleMessage(ServletMessage message) throws Throwable {
        String installRedirectUri = StringUtils.appendIfMissing(this.haloProperties.getAdminPath(), "/") + "index.html#install";
        message.getResponse().sendRedirect(installRedirectUri);
        message.getAsyncContext().complete();

        return null;
    }
}
