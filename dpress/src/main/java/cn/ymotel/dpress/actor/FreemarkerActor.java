package cn.ymotel.dpress.actor;

import cn.ymotel.dactor.Constants;
import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.Message;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.filter.RecordHttpResponseWrapper;
import cn.ymotel.dpress.template.MultiDomainFreeMarkerView;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.swing.text.View;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class FreemarkerActor implements Actor<ServletMessage> {
    @Autowired
    private MultiDomainFreeMarkerView multiDomainFreeMarkerView;
//    @Autowired
//    private SqlSession sqlSession;
//    private Map getSiteId(Message message){
//        ServletMessage localServletMessage=(ServletMessage)message;
//        String domain= localServletMessage.getRequest().getServerName();
//        /**
//         * 得到域名,
//         */
//        Map tMap=new HashMap();
//        tMap.put("domain",domain);
//        Map rtnMap=sqlSession.selectOne("dpress.qsiteid",tMap);
//        CONTEXT_HOLDER.set(rtnMap);
//        return rtnMap;
//    }
//    private Object getSiteId(HttpServletRequest request){
//        String domain=request.getServerName();
//        /**
//         * 得到域名,
//         */
//        Map tMap=new HashMap();
//        tMap.put("domain",domain);
//        Map rtnMap=sqlSession.selectOne("dpress.qsiteid",tMap);
//        if(rtnMap==null||rtnMap.isEmpty()){
//
//        }else{
//            String ssid =  rtnMap.get("id").toString();
//            if(ssid!=null){
//                CONTEXT_HOLDER.set(ssid);
//            }
//            return ssid;
//        }
//        return null;
//     }
    @Override
    public Object HandleMessage(ServletMessage message) throws Throwable {
//        Map siteMap= getSiteId(message);
        message.getAsyncContext().addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent event) throws IOException {

                RecordHttpResponseWrapper wrapper =
                        WebUtils.getNativeResponse(event.getAsyncContext().getResponse(), RecordHttpResponseWrapper.class);
                if(wrapper!=null) {
                    wrapper.putCache();
                }
//            System.out.println(wrapper);
            }

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {

            }

            @Override
            public void onError(AsyncEvent event) throws IOException {

            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {

            }
        });
        ;
        long begin=System.currentTimeMillis();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(message.getRequest(),message.getResponse()),false);
        try {
            Object obj = Execute(message);
            long end=System.currentTimeMillis();
//            System.out.println("count time--"+(end-begin)/1000);
            if(obj==null){
                return message;
            }
            if(obj instanceof ViewData){
                ViewData viewData=(ViewData)obj;
                ;
               Object id= Utils.getSiteId();
                if(viewData.getContentType()!=null) {
                    message.getResponse().setContentType(viewData.getContentType());
                }else {
                    message.getResponse().setContentType("text/html; charset=utf-8");

                }

                java.util.Date date = new java.util.Date();
                message.getResponse().setDateHeader("Last-Modified",date.getTime()); //Last-Modified:页面的最后生成时间
                message.getResponse().setDateHeader("Expires",date.getTime()+20000); //Expires:过时期限值
                message.getResponse().setHeader("Cache-Control", "public"); //Cache-Control来控制页面的缓存与否,public:浏览器和缓存服务器都可以缓存页面信息

                message.getResponse().setCharacterEncoding("UTF-8");


                String content = multiDomainFreeMarkerView.getProcessedString(message,id,(String)viewData.getViewName(),(Map)viewData.getData());

//                message.getResponse().getWriter().print(content);
                message.getResponse().getWriter().flush();
                message.getAsyncContext().complete();
                return message;
            }
            if (obj != null) {
                message.getContext().put(Constants.CONTENT, obj);
            }
        } catch (Throwable e) {
                e.printStackTrace();
            message.setException(e);
        }finally {

            long end=System.currentTimeMillis();

//            System.out.println("final time--"+(end-begin)/1000);

        }
        return message;
    }
}
