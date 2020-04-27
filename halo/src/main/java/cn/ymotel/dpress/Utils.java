package cn.ymotel.dpress;

import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dpress.actor.FreemarkerActor;
import cn.ymotel.dpress.admin.sitemgmt.ChangeCurrentSiteActor;
import cn.ymotel.dpress.filter.SiteIdFilter;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class Utils {
    public static Long getSiteIdFromMvc(){
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        if(ra==null){
            return null;
        }
        HttpServletRequest request = ((ServletRequestAttributes)ra).getRequest();
        return Long.parseLong(request.getSession().getAttribute(ChangeCurrentSiteActor.SESSION_SITE).toString());
    }
    public static long getSiteIdFromMessage(ServletMessage message){
        HttpServletRequest request=message.getRequest();
        return Long.parseLong(request.getSession().getAttribute(ChangeCurrentSiteActor.SESSION_SITE).toString());
    }
    public static Object getSiteId(){
        Map map=FreemarkerActor.CONTEXT_HOLDER.get();
        if(map==null){
            return SiteIdFilter.getSiteId();
        }
        Object siteid= map.get("id");
            return siteid;
    }
    public  static String getBaseUrl(ServletMessage message){
        HttpServletRequest request= message.getRequest();
        String path = request.getContextPath();



        String scheme=request.getScheme();
        String baseUrl= request.getScheme()+"://"+request.getServerName();
        if(scheme.equalsIgnoreCase("http")){
            if(request.getServerPort()==80){

            }else{
                baseUrl=baseUrl+":"+request.getServerPort();
            }
            baseUrl=baseUrl+path;
            return baseUrl;
        }
        if(scheme.equalsIgnoreCase("https")){
            if(request.getServerPort()==443){

            }else{
                baseUrl=baseUrl+":"+request.getServerPort();
            }
            baseUrl=baseUrl+path;
            return baseUrl;
        }
        return "";
    }
}
