package cn.ymotel.dpress.actor;

import ch.qos.logback.core.util.ContextUtil;
import cn.ymotel.dactor.Constants;
import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.async.web.ContentTypeUtil;
import cn.ymotel.dactor.message.Message;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.service.SiteThemeService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@ActorCfg(urlPatterns = "/themes/**")
public class ThemeStaticResourceActor implements Actor<ServletMessage> {


    @Autowired
    SiteThemeService siteThemeService;
    @Override
    public Object HandleMessage(ServletMessage message) throws Throwable {
        String activeName=siteThemeService.getActiveThemeName(Utils.getSiteId());
        String path=message.getControlData(Constants.EXTRACTPATH);
       Object content= siteThemeService.getContent(Utils.getSiteId(),activeName,path);
        MediaType mediaType= ContentTypeUtil.getMediaType((HttpServletRequest) message.getAsyncContext().getRequest(),path);
        HttpServletResponse response=(HttpServletResponse)message.getAsyncContext().getResponse();
        if(mediaType!=null){
            response.setContentType(mediaType.toString());
        }
        if(content instanceof  String){
            response.getWriter().print((String)content);
            response.getWriter().flush();
        }
        if(content instanceof  byte[]){
            response.getOutputStream().write((byte[])content);
            response.flushBuffer();
        }
        message.getAsyncContext().complete();
        return null;
    }
}
