package cn.ymotel.dpress.admin.theme;

import cn.ymotel.dactor.Constants;
import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/themes/{themeId}/files/content.json")
public class FileContentActor  implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Object Execute(ServletMessage message) throws Throwable {
        String METHOD=message.getContextData(Constants.METHOD);
       String theme=getActiveTheme(message);


        if(METHOD.equalsIgnoreCase("GET")) {
            Map tMap = new HashMap();
            tMap.put("siteid", Utils.getSiteIdFromMessage(message));
            tMap.put("path", message.getContextData("path"));
            tMap.put("theme",theme);
            Map dbMap = sqlSession.selectOne("dpress.qtemplate", tMap);
            String content = (String) dbMap.get("content");
            if (content == null) {
                content = new String((byte[]) dbMap.get("bcontent"));
            }
            Map rtnMap = new HashMap();
            rtnMap.put("data", content);
            return rtnMap;
        }
        if(METHOD.equalsIgnoreCase("PUT")){
           Map context= message.getContext();
           context.put("siteid", Utils.getSiteIdFromMessage(message));
            context.put("theme",theme);
            context.put("lastModified",new java.sql.Timestamp(System.currentTimeMillis()));
            sqlSession.update("dpress.utemplatecontent",context);
            Map rtnMap = new HashMap();
            rtnMap.put("data", null);
            return rtnMap;
        }
        return null;
    }
    public String getActiveTheme(ServletMessage message){

            Map tMap = new HashMap();
            tMap.put("siteid", Utils.getSiteIdFromMessage(message));
            Map themeMap = sqlSession.selectOne("options.qactivetheme", tMap);
            String theme = (String) themeMap.get("option_value");
            return theme;

    }
}
