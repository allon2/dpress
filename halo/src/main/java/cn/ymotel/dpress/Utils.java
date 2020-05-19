package cn.ymotel.dpress;

import cn.ymotel.dactor.core.MessageThreadLocal;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dpress.actor.FreemarkerActor;
import cn.ymotel.dpress.admin.sitemgmt.ChangeCurrentSiteActor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
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
        ServletMessage message=(ServletMessage)MessageThreadLocal.getMessage();
        HttpServletRequest request=((HttpServletRequest)message.getAsyncContext().getRequest());
        {
            Long siteid = preview(request);
            if(siteid!=null){
                return siteid;
            }
        }

        Object obj=getAdminSiteId();
        if(obj!=null){
            return obj;
        }

        if(message==null){
            Object id= FreemarkerActor.CONTEXT_HOLDER.get();
            if(id!=null){
                return id;
            }

            return null;
        }



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
    public static Long preview(HttpServletRequest request){
        if(request.getMethod().equals("GET")){

        }else{
            return null;
        }

       String referer= request.getHeader("referer");
        if(referer==null){
            return null;
        }
        String token=null;
       if(referer.startsWith(getBaseUrl(request)+"/admin/index.html")){
          token=   request.getParameter("token");
       }else{
           UrlUtil.UrlEntity urlEntity= UrlUtil.parse(referer);
            token=urlEntity.params.get("token");
       }
       if(token==null){
           return null;
       }
       Map map=(Map)request.getSession().getAttribute("_Preiview_Token");
       if(map==null||map.isEmpty()){
           return null;
       }
       Long siteid=(Long)map.get(token);
       return siteid;
    }
    public static String getBaseUrl(HttpServletRequest request){
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
    public  static String getBaseUrl(ServletMessage message){
        HttpServletRequest request= message.getRequest();
        return getBaseUrl(request);
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

    /**
     *
     * @return jar jar:同级目录,War:WEB-INF下级目录
     * @throws FileNotFoundException
     */
    public static File getJdbcFile() throws FileNotFoundException {
        //file:/E:/OpenSource/blog/halo/20200324/halo/build/libs/halo-1.3.0-beta.3.jar!/BOOT-INF/classes!/
//        String jar_parent = new File(ResourceUtils.getURL("classpath:").getPath()).getParentFile().getParentFile().getParent();


        ApplicationHome h = new ApplicationHome(Utils.class);
        File jarF = h.getSource();
//        System.out.println(jarF.getParentFile().toString());
        String path=jarF.getParentFile().toString();
        System.out.println(path);

//        String path = Utils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//        System.out.println(path);
        return new File(path+"/jdbc.properties");
    }
    private static boolean isinstall=false;
    public static Boolean isInstall() {
        if(isinstall){
            return true;
        }
        try {
            if(getJdbcFile().exists()){
                isinstall=true;
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return  false;
//        String path = Utils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//        System.out.println("path----"+path);
//        return false;
    }
}
