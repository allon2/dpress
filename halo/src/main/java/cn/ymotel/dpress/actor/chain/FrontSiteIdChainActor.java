package cn.ymotel.dpress.actor.chain;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.BeforeChain;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@BeforeChain(chain = "publicchain")
public class FrontSiteIdChainActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
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
    public <E> E Execute(ServletMessage message) throws Throwable {
            SaveSiteId(message.getRequest());
            return null;
    }
}
