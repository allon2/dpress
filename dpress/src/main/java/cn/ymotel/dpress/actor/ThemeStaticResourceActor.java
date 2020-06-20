package cn.ymotel.dpress.actor;

import ch.qos.logback.core.util.ContextUtil;
import cn.ymotel.dactor.Constants;
import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.async.web.ContentTypeUtil;
import cn.ymotel.dactor.message.Message;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.filter.RecordHttpResponseWrapper;
import cn.ymotel.dpress.service.SiteThemeService;
import org.apache.ibatis.session.SqlSession;
import org.apache.tika.Tika;
import org.apache.tika.detect.AutoDetectReader;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.util.WebUtils;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.HashMap;
import java.util.Map;

@ActorCfg(urlPatterns = "/themes/**",chain = "publicchain")
public class ThemeStaticResourceActor implements Actor<ServletMessage> {


    @Autowired
    SiteThemeService siteThemeService;
    @Override
    public Object HandleMessage(ServletMessage message) throws Throwable {
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
        String activeName=siteThemeService.getActiveThemeName(Utils.getSiteId());
        String path=message.getControlData(Constants.EXTRACTPATH);
        Map dataMap=siteThemeService.getThemeInfo(Utils.getSiteId(),activeName,path);
       Object content= siteThemeService.getContent(dataMap,path);
        String encoding=(String)dataMap.get("encoding");
        String mediatype=(String)dataMap.get("mediatype");
//        guessEncoding(content);

        MediaType mediaType= ContentTypeUtil.getMediaType((HttpServletRequest) message.getAsyncContext().getRequest(),path);
        HttpServletResponse response=(HttpServletResponse)message.getAsyncContext().getResponse();


        java.util.Date date = new java.util.Date();
         response.setDateHeader("Last-Modified",date.getTime()); //Last-Modified:页面的最后生成时间
          response.setDateHeader("Expires",date.getTime()+20000); //Expires:过时期限值
          response.setHeader("Cache-Control", "public"); //Cache-Control来控制页面的缓存与否,public:浏览器和缓存服务器都可以缓存页面信息

        {
            String contentTpe=null;
        if(mediaType!=null){
            contentTpe=mediaType.toString();
        }else{
            contentTpe=mediatype;
        }
            if(encoding!=null){
                contentTpe=contentTpe+";charset="+encoding;
            }
            response.setContentType(contentTpe);

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
