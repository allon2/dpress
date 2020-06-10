package cn.ymotel.dpress.admin.sitemgmt;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.Message;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.admin.install.InstallSiteActor;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

@ActorCfg(urlPatterns = "/api/admin/site/create.json")
public class SiteCreateActor implements Actor {
    @Autowired
    private SqlSession sqlSession;
    @Autowired
    private InstallSiteActor installSiteActor;
    @Override
    public Object Execute(Message message) throws Throwable {
        Object siteid=IdWorker.getId();
        message.getContext().put("id",siteid);
        sqlSession.insert("dpress.isiteinfo",message.getCaseInsensitivegetContext());
        String serverName=message.getContextData("domain");
        String title=message.getContextData("sitename");
        message.getContext().put("title",title);
//        installSiteActor.createSiteInfo(siteid,serverName,title);
        installSiteActor.initSetting(siteid,message.getContext());
        installSiteActor. createCategories(siteid);
        Object postid=installSiteActor.createDefaultPost(siteid);
        installSiteActor.  createDefaultComment(siteid,postid);
        installSiteActor. createDefaultSheet(siteid);
        installSiteActor.createDefaultMenu(siteid);
//        installSiteActor.InstallDefaultThemes(siteid);
        installSiteActor.installDefaultTheme(siteid,null);
        installSiteActor.activeTheme(siteid,null);

        return new HashMap<>();
    }
}
