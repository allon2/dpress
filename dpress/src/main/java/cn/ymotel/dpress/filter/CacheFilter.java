package cn.ymotel.dpress.filter;

import cn.ymotel.dpress.Utils;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Component
//@WebFilter()
//@Order(HIGHEST_PRECEDENCE)

public class CacheFilter  implements Filter {
    @CreateCache(cacheType = CacheType.LOCAL)
    private Cache<String, CacheResponseEntity> responseCache;
    private UrlPathHelper urlPathHelper=new UrlPathHelper();
    private AntPathMatcher antPathMatcher=new AntPathMatcher();
    private List<String> list=new ArrayList<>();
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

       String path=  urlPathHelper.getLookupPathForRequest((HttpServletRequest) request);
       String domainpath=Utils.getBaseUrl((HttpServletRequest)request)+path;
        for(int i=0;i<list.size();i++){
            if(antPathMatcher.match(list.get(i),path)){
                CacheResponseEntity entity= responseCache.get(domainpath);
                if(entity!=null){
                    response.setContentType(entity.getContentType());
                    response.setCharacterEncoding(entity.getEncoding());
                    response.getOutputStream().write(entity.getContent());
                    response.getOutputStream().flush();
                }else{
//                    entity=new CacheResponseEntity();
//                    responseCache.put(path,entity);
                    chain.doFilter(request,new RecordHttpResponseWrapper((HttpServletResponse) response,domainpath,responseCache));
                }
                return;
            }
        }
        chain.doFilter(request,response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        list.add("/themes/**");
        list.add("/");
        urlPathHelper.setAlwaysUseFullPath(true);
    }
}
