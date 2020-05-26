package cn.ymotel.dpress.admin.theme;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import run.halo.app.exception.NotFoundException;

import java.util.HashMap;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/themes/{themeId}/activation",methods = RequestMethod.POST)
public class ThemeActivationActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Object Execute(ServletMessage message) throws Throwable {

        Map map=new HashMap();
        map.put("key","installthemes");
        map.put("siteid",Utils.getSiteIdFromMessage(message));

        java.util.Map installthemesMap=sqlSession.selectOne("options.qoption",map);
        String themes=(String)installthemesMap.get("option_value");
        String themeId=message.getContextData("themeId");
        if(themes.indexOf(themeId)>=0){

        }else{
            throw new NotFoundException("没有找到 id 为 " + themeId + " 的主题").setErrorData(themeId);
        }
        map.put("option_key","theme");
        map.put("option_value",themeId);
        map.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
        sqlSession.update("options.uoption",map);

        return new HashMap<>();

    }


}
