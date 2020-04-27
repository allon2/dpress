package cn.ymotel.dpress.filter;

import cn.ymotel.dactor.message.Message;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dpress.actor.FreemarkerActor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import run.halo.app.security.context.SecurityContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Component
public class SiteIdFilter implements Filter {
    @Autowired
    private SqlSession sqlSession;
    public static Object getSiteId() {
        // Get from thread local
        Object context = FreemarkerActor.CONTEXT_HOLDER.get();

        return context;
    }
    private Map getSiteId(HttpServletRequest request){
        String domain=request.getServerName();
        /**
         * 得到域名,
         */
        Map tMap=new HashMap();
        tMap.put("domain",domain);
        Map rtnMap=sqlSession.selectOne("dpress.qsiteid",tMap);
        if(rtnMap==null||rtnMap.isEmpty()){

        }else{
            String ssid =  rtnMap.get("id").toString();
            if(ssid!=null){
                FreemarkerActor.CONTEXT_HOLDER.set(rtnMap);

            }
        }
        return rtnMap;
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
         getSiteId((HttpServletRequest) request);

       chain.doFilter(request,response);

    }
}
