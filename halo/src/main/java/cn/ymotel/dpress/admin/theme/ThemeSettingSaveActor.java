package cn.ymotel.dpress.admin.theme;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@ActorCfg(urlPatterns = "/api/admin/themes/{_themeId}/settings",methods = RequestMethod.POST)
public class ThemeSettingSaveActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Object Execute(ServletMessage message) throws Throwable {
      Map params=  message.getContext();
      String theme_id=message.getContextData("_themeId");
        params.forEach((key, value) -> {
            if(key.toString().startsWith("_")){

            }else{
                value=value+"";
//                if(value instanceof  Boolean){
//                    value=Boolean.
//                }
                Map tMap=new HashMap();
                tMap.put("siteid",Utils.getSiteIdFromMessage(message));
                tMap.put("setting_key",key);
                tMap.put("setting_value",value);
                tMap.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
                tMap.put("theme_id",theme_id);
                int count=sqlSession.update("theme_settings.ubyvalue",tMap);
                if(count==0){
                    tMap.put("create_time",new java.sql.Timestamp(System.currentTimeMillis()));
                    sqlSession.insert("theme_settings.iall",tMap);
                }
            }
        });

        return new HashMap<>();

    }


}
