package cn.ymotel.dpress.filter;

import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.actor.FreemarkerActor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class SiteIdFilter implements Filter {
    @Autowired
    private SqlSession sqlSession;
    private UrlPathHelper urlPathHelper=new UrlPathHelper();
    private AntPathMatcher antPathMatcher=new AntPathMatcher();
    private List<String> installSkipList=new ArrayList<>();
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        urlPathHelper.setAlwaysUseFullPath(false);
        antPathMatcher.setCaseSensitive(false);
        installSkipList.add("/admin/**");
        installSkipList.add("/api/admin/installations");
        installSkipList.add("/api/admin/is_installed");
        installSkipList.add("/install");

    }

    private void SaveSiteId(HttpServletRequest request){
        if( request.getSession().getAttribute(Utils.FRONT_SESSION_SITEID)!=null){
            FreemarkerActor.CONTEXT_HOLDER.set(request.getSession().getAttribute(Utils.FRONT_SESSION_SITEID));
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
                request.getSession().setAttribute(Utils.FRONT_SESSION_SITEID,ssid);
                FreemarkerActor.CONTEXT_HOLDER.set(ssid);

            }
        }
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
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Boolean isInstalled= Utils.isInstall();
        String path=urlPathHelper.getLookupPathForRequest((HttpServletRequest) request);
        if(!isInstalled){//未安装
            if(isInStallPath(path)){
                chain.doFilter(request,response);
            }else{
                               ((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath() + "/install");
            }
            return ;
        }
        {
            if(path.startsWith("/api/admin")){

                chain.doFilter(request,response);
                    return ;
            }

        }
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes((HttpServletRequest) request,(HttpServletResponse) response),false);
        SaveSiteId((HttpServletRequest) request);

       chain.doFilter(request,response);
        FreemarkerActor.CONTEXT_HOLDER.remove();

    }
}
