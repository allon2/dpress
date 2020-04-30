package cn.ymotel.dpress.filter;

import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.actor.FreemarkerActor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Component
public class SiteIdFilter implements Filter {
    @Autowired
    private SqlSession sqlSession;

    private void SaveSiteId(HttpServletRequest request){
        if( request.getSession().getAttribute(Utils.SESSION_SITEID)!=null){
            FreemarkerActor.CONTEXT_HOLDER.set(request.getSession().getAttribute(Utils.SESSION_SITEID));
            return;
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
                request.getSession().setAttribute(Utils.SESSION_SITEID,ssid);
                FreemarkerActor.CONTEXT_HOLDER.set(ssid);

            }
        }
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes((HttpServletRequest) request,(HttpServletResponse) response),false);
        SaveSiteId((HttpServletRequest) request);

       chain.doFilter(request,response);
        FreemarkerActor.CONTEXT_HOLDER.remove();

    }
}
