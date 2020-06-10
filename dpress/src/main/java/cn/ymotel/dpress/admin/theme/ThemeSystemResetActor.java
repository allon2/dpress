package cn.ymotel.dpress.admin.theme;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.entity.mapper.DpressTemplateMapper;
import cn.ymotel.dpress.entity.mapper.ThemeSettingsMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/themes/systemreset/{_themeId}",methods = RequestMethod.DELETE)
public class ThemeSystemResetActor implements Actor<ServletMessage> {
    @Autowired
    SqlSession sqlSession;
    @Override
    public Map Execute(ServletMessage message) throws Throwable {
        Object siteid= Utils.getSiteIdFromMessage(message);

        Map map=new HashMap();
        map.put("siteid",siteid);
        map.put("theme_id",message.getContextData("_themeId"));
        sqlSession.getMapper(ThemeSettingsMapper.class).deleteByMap(map);
        map.remove("theme_id");
        map.put("theme",message.getContextData("_themeId"));
        sqlSession.getMapper(DpressTemplateMapper.class).deleteByMap(map);
        return new HashMap<>();
    }
}
