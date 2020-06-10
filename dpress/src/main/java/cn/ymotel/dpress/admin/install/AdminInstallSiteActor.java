package cn.ymotel.dpress.admin.install;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import org.springframework.beans.factory.annotation.Autowired;

@ActorCfg(urlPatterns = "/api/admin/siteinstall",chain = "publicchain")
public class AdminInstallSiteActor  implements Actor<ServletMessage> {
    @Autowired
    private InstallSiteActor installSiteActor;

    @Override
    public Object Execute(ServletMessage message) throws Throwable {
       Object siteid= message.getContextData("siteid");
        String url=message.getContextData("url");
        String title=message.getContextData("title");
        String serverName=installSiteActor.getServerName(url);
        installSiteActor.createSiteInfo(siteid,serverName,title);
        installSiteActor.initSetting(siteid,message.getContext());
        installSiteActor. createCategories(siteid);
        Object postid=installSiteActor.createDefaultPost(siteid);
        installSiteActor.  createDefaultComment(siteid,postid);
        installSiteActor. createDefaultSheet(siteid);
        installSiteActor.createDefaultMenu(siteid);
//        installSiteActor.InstallDefaultThemes(siteid);

        installSiteActor.installDefaultTheme(siteid,null);
        installSiteActor.activeTheme(siteid,null);
        return "安装完成！";
    }
}
