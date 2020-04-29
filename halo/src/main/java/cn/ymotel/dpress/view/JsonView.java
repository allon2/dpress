package cn.ymotel.dpress.view;

import cn.ymotel.dactor.Constants;
import cn.ymotel.dactor.async.web.view.CustomHttpView;
import cn.ymotel.dactor.message.Message;
import cn.ymotel.dactor.message.ServletMessage;
import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class JsonView  implements CustomHttpView<ServletMessage> {


    @Override
    public void successRender(ServletMessage message, String viewName) {
        Object obj=message.getContextData(Constants.CONTENT);
        Map rtnMap=new HashMap();
        rtnMap.put("data",obj);
        rtnMap.put("status",HttpStatus.OK.value());
        rtnMap.put("message",HttpStatus.OK.getReasonPhrase());
       String json= JSON.toJSONString(rtnMap);
        writeString(message,json,null);

    }

    @Override
    public void exceptionRender(ServletMessage message, String viewName) {
        message.getException();
        Map map=new HashMap();
        map.put("message",message.getException().getMessage());
        map.put("status", HttpStatus.BAD_REQUEST.value());
        String json= JSON.toJSONString(map);
        writeString(message,json,HttpStatus.BAD_REQUEST);
    }
    public void writeString(ServletMessage message,String string,HttpStatus status){
        try {
            HttpServletResponse response= (HttpServletResponse)message.getAsyncContext().getResponse();
           if(response.isCommitted()){
               return;
           }
            if(status!=null) {
                response.setStatus(status.value(),status.getReasonPhrase());
            }
            message.getAsyncContext().getResponse().setContentType("application/json;charset=UTF-8");
            message.getAsyncContext().getResponse().getWriter().print(string);
            message.getAsyncContext().getResponse().getWriter().flush();
            message.getAsyncContext().complete();
        } catch (IOException e) {

        }
    }

    @Override
    public String getUrlPattern() {
        return "/api/admin/**";
    }

}
