package cn.ymotel.dpress;

import cn.ymotel.dactor.core.MessageThreadLocal;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dpress.actor.FreemarkerActor;
import cn.ymotel.dpress.admin.sitemgmt.ChangeCurrentSiteActor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static run.halo.app.model.support.HaloConst.ADMIN_TOKEN_HEADER_NAME;
import static run.halo.app.model.support.HaloConst.ADMIN_TOKEN_QUERY_NAME;

public class Utils {
    public static  String FRONT_SESSION_SITEID ="_FRONT_SITEID";
    public static String ADMIN_SITEID="_ADMIN_STIEID";
    public static Long getSiteIdFromMvc(){
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        if(ra==null){
            return null;
        }
        HttpServletRequest request = ((ServletRequestAttributes)ra).getRequest();
        Object obj=request.getSession().getAttribute(ADMIN_SITEID);
        return Long.parseLong(obj.toString());
    }
    public static long getSiteIdFromMessage(ServletMessage message){
        HttpServletRequest request=message.getRequest();
        return Long.parseLong(request.getSession().getAttribute(ADMIN_SITEID).toString());
    }
    public static  String getTokenFromRequest(@NonNull HttpServletRequest request, @NonNull String tokenQueryName, @NonNull String tokenHeaderName) {
        Assert.notNull(request, "Http servlet request must not be null");
        Assert.hasText(tokenQueryName, "Token query name must not be blank");
        Assert.hasText(tokenHeaderName, "Token header name must not be blank");

        // Get from header
        String accessKey = request.getHeader(tokenHeaderName);

        // Get from param
        if (StringUtils.isBlank(accessKey)) {
            accessKey = request.getParameter(tokenQueryName);
        } else {
        }

        return accessKey;
    }
    public static  Object getAdminSiteId(){

        ServletRequestAttributes servletRequestAttributes= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(servletRequestAttributes==null){
            return null;
        }
        HttpServletRequest request =
                servletRequestAttributes.getRequest();
        if(request!=null){
            String token= getTokenFromRequest(request, ADMIN_TOKEN_QUERY_NAME, ADMIN_TOKEN_HEADER_NAME);
            if(token!=null){
                Object obj=request.getSession().getAttribute(ADMIN_SITEID);
                return obj;
            }

        }
        return null;
    }
    public static Object getSiteId(){
        Object obj=getAdminSiteId();
        if(obj!=null){
            return obj;
        }

        ServletMessage message=(ServletMessage)MessageThreadLocal.getMessage();
        if(message==null){
            Object id= FreemarkerActor.CONTEXT_HOLDER.get();
            if(id!=null){
                return id;
            }

            return null;
        }
        HttpServletRequest request=((HttpServletRequest)message.getAsyncContext().getRequest());


        String token= getTokenFromRequest(request, ADMIN_TOKEN_QUERY_NAME, ADMIN_TOKEN_HEADER_NAME);
        if(token!=null){
            return               request.getSession().getAttribute(ADMIN_SITEID);

        }
        return  request.getSession().getAttribute(FRONT_SESSION_SITEID);
//        if(map==null){
//            return SiteIdFilter.getSiteId();
//        }
//        Object siteid= map.get("id");
//            return siteid;
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
    public static void WriteMysqlInfo(String url,String userName,String passwd) throws IOException {
        File f=getJdbcFile();
        if(!f.exists()){
            f.createNewFile();
            Properties properties = new Properties();
            properties.put("url",url);
            properties.put("username",userName);
            properties.put("password",passwd);
            FileOutputStream output = new FileOutputStream(f);
            properties.store(output,"update properties");
        }
    }
    public static File getJdbcFile(){
        String path = Utils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        System.out.println(path);
        return new File(path+"/jdbc.properties");
    }
    public static Boolean isInstall() {
        if(getJdbcFile().exists()){
            return true;
        }
        return  false;
//        String path = Utils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//        System.out.println("path----"+path);
//        return false;
    }
}
