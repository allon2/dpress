package cn.ymotel.dpress.view;

import cn.ymotel.dactor.Constants;
import cn.ymotel.dactor.async.web.view.CustomHttpView;
import cn.ymotel.dactor.message.Message;
import cn.ymotel.dactor.message.ServletMessage;
import com.alibaba.fastjson.JSON;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import run.halo.app.model.support.CommentPage;

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
        if(obj instanceof Page){
            Page page=(Page)obj;
            Map dataMap=new HashMap();
            dataMap.put("content",page.getContent());
            dataMap.put("pages",page.getTotalPages());
            dataMap.put("total",page.getTotalElements());
            dataMap.put("page",page.getNumber());
            dataMap.put("rpp",page.getSize());
            dataMap.put("hasNext",page.hasNext());
            dataMap.put("hasPrevious",page.hasPrevious());
            dataMap.put("isFirst",page.isFirst());
            dataMap.put("isLast",page.isLast());
            dataMap.put("isEmpty",page.isEmpty());
            dataMap.put("hasContent",page.hasContent());
            if (obj instanceof CommentPage) {
                CommentPage commentPage = (CommentPage) page;
                dataMap.put("commentPage",commentPage.getCommentCount());
            }
            rtnMap.put("data", dataMap);

        }else {
            rtnMap.put("data", obj);
        }

        rtnMap.put("status",HttpStatus.OK.value());
        rtnMap.put("message",HttpStatus.OK.getReasonPhrase());
       String json= JSON.toJSONString(rtnMap);
        writeString(message,json,null);

    }

    @Override
    public void exceptionRender(ServletMessage message, String viewName) {
        message.getException().printStackTrace();
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
