package cn.ymotel.dpress.admin.theme;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.Map;

@ActorCfg(urlPatterns = {"/api/admin/themes/activation"})
public class ActivationThemeActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    public String getActiveTheme(ServletMessage message){

        Map tMap = new HashMap();
        tMap.put("siteid", Utils.getSiteIdFromMessage(message));
        Map themeMap = sqlSession.selectOne("options.qactivetheme", tMap);
        String theme = (String) themeMap.get("option_value");
        return theme;

    }
    @Override
    public Object Execute(ServletMessage message) throws Throwable {
        String activeTheme=getActiveTheme(message);

        Map map=new HashMap();
        map.put("siteid",Utils.getSiteIdFromMessage(message));
        map.put("path","theme.yaml");
        map.put("theme",activeTheme);
        Map  tmpthemeMap= sqlSession.selectOne("dpress.qtemplate",map);

        String content=(String) tmpthemeMap.get("content");
        Yaml yaml = new Yaml();
        Map yamlmap =  yaml.load(content);
        if(yamlmap.get("id").equals(activeTheme)){
            yamlmap.put("activated",true);
        }else{
            yamlmap.put("activated",false);
        }
        yamlmap.put("screenshots","/themes/"+yamlmap.get("id")+"/screenshot");
        return  yamlmap;
    }
}
