package cn.ymotel.dpress.admin.theme;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@ActorCfg(urlPatterns = "/themes/usertheme/{themeId}/screenshot",view = "img:")
public class UserThemeScreenshotActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Object Execute(ServletMessage message) throws Throwable {
        Map map=new HashMap();
        map.put("siteid",Utils.getSiteIdFromMessage(message));
        map.put("path","screenshot.png");
        map.put("theme",message.getContextData("themeId"));

        Map  dataMap= sqlSession.selectOne("dpress.qtemplate",map);
        return dataMap.get("bcontent");


    }


}
