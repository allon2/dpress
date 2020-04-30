package cn.ymotel.dpress.actor;

import cn.ymotel.dactor.Constants;
import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.Message;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dpress.template.MultiDomainFreeMarkerView;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.View;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class FreemarkerActor implements Actor<ServletMessage> {
    public final static ThreadLocal<Object> CONTEXT_HOLDER = new ThreadLocal<>();
    @Autowired
    private MultiDomainFreeMarkerView multiDomainFreeMarkerView;
    @Autowired
    private SqlSession sqlSession;
    private Map getSiteId(Message message){
        ServletMessage localServletMessage=(ServletMessage)message;
        String domain= localServletMessage.getRequest().getServerName();
        /**
         * 得到域名,
         */
        Map tMap=new HashMap();
        tMap.put("domain",domain);
        Map rtnMap=sqlSession.selectOne("dpress.qsiteid",tMap);
        CONTEXT_HOLDER.set(rtnMap);
        return rtnMap;
    }
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
        Map siteMap= getSiteId(message);

        ;
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(message.getRequest(),message.getResponse()),false);
        try {
            Object obj = Execute(message);
            if(obj==null){
                return message;
            }
            if(obj instanceof ViewData){
                ViewData viewData=(ViewData)obj;
                ;
               Object id=siteMap.get("id");

                String content = multiDomainFreeMarkerView.getProcessedString(message,id,(String)viewData.getViewName(),(Map)viewData.getData());
                if(viewData.getContentType()!=null) {
                    message.getResponse().setContentType(viewData.getContentType());
                }else {
                    message.getResponse().setContentType("text/html; charset=utf-8");

                }
                message.getResponse().setCharacterEncoding("UTF-8");
                message.getResponse().getWriter().print(content);
                message.getResponse().getWriter().flush();
                message.getAsyncContext().complete();
                return null;
            }
            if (obj != null) {
                message.getContext().put(Constants.CONTENT, obj);
            }
        } catch (Throwable e) {
                e.printStackTrace();
            message.setException(e);
        }finally {
            CONTEXT_HOLDER.set(null);
        }
        return message;
    }
}
