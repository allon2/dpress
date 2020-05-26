package cn.ymotel.dpress.actor.chain;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.BeforeChain;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@BeforeChain(chain = "publicchain")
public class FrontSiteIdChainActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    private List<String> installSkipList=new ArrayList<>();
    private UrlPathHelper urlPathHelper=new UrlPathHelper();
    private AntPathMatcher antPathMatcher=new AntPathMatcher();

    public FrontSiteIdChainActor() {
        urlPathHelper.setAlwaysUseFullPath(false);
        antPathMatcher.setCaseSensitive(false);
        installSkipList.add("/admin/**");
        installSkipList.add("/api/admin/installations");
        installSkipList.add("/api/admin/is_installed");
        installSkipList.add("/install");
    }
    private boolean isInStallPath(String path){
        for(int i=0;i<installSkipList.size();i++){
            boolean b= antPathMatcher.match(installSkipList.get(i),path);
            if(b){
                return true;
            }
        }
        return false;
    }
    private void SaveSiteId(HttpServletRequest request) {
        if( request.getSession().getAttribute(Utils.FRONT_SESSION_SITEID)!=null) {
            return ;
        }
            String domain=request.getServerName();
        /**
         * 得到域名,
         */
        Map tMap=new HashMap();
        tMap.put("domain",domain);
        Map rtnMap=sqlSession.selectOne("dpress.qsiteid",tMap);
        if(rtnMap==null||rtnMap.isEmpty()){

        }else{
            Object ssid =  rtnMap.get("id") ;
            if(ssid!=null){
                request.getSession().setAttribute(Utils.FRONT_SESSION_SITEID,ssid);

            }
        }
    }

    @Override
    public ServletMessage HandleMessage(ServletMessage message) throws Throwable {
        Boolean isInstalled= Utils.isInstall();
        if(!isInstalled) {//未安装
            String path=urlPathHelper.getLookupPathForRequest(message.getRequest());
            if(isInStallPath(path)){
                return  message;
            }else {
                message.getResponse().sendRedirect(message.getRequest().getContextPath() + "/install");
                message.getAsyncContext().complete();
            }
            return null;
        }
        SaveSiteId(message.getRequest());
        return  message;
    }


}
