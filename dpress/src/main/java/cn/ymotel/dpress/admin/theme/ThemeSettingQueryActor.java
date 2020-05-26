package cn.ymotel.dpress.admin.theme;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.yaml.snakeyaml.Yaml;
import run.halo.app.model.enums.DataType;
import run.halo.app.model.enums.InputType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@ActorCfg(urlPatterns = "/api/admin/themes/{_themeId}/settings",methods = RequestMethod.GET)
public class ThemeSettingQueryActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Object Execute(ServletMessage message) throws Throwable {
        Map<String ,DataType> itemsDataType= getItems(message);

        Map tMap=new HashMap();
        tMap.put("siteid",Utils.getSiteIdFromMessage(message));
        tMap.put("theme_id",message.getContextData("_themeId"));
        List<Map> ls= sqlSession.selectList("theme_settings.qall",tMap);
        Map rtnMap=new HashMap();
        ls.forEach(map -> {

            rtnMap.put(map.get("setting_key"),itemsDataType.get(map.get("setting_key")).convertTo(map.get("setting_value")));
        });
        return rtnMap;




    }
    public Map<String ,DataType> getItems(ServletMessage message){
        Map map=new HashMap();
        map.put("path","settings.yaml");
        map.put("theme",message.getContextData("_themeId"));
        map.put("siteid",Utils.getSiteIdFromMessage(message));
        Map  tmpthemeMap= sqlSession.selectOne("dpress.qtemplate",map);
        if(tmpthemeMap==null){

        }
        Map itemMap=new HashMap();
        String content=(String) tmpthemeMap.get("content");
        Yaml yaml = new Yaml();
        Map yamlmap =  yaml.load(content);
        yamlmap.forEach((key, value) -> {
            Map items=(Map)((Map)value).get("items");
            for(java.util.Iterator iter=items.entrySet().iterator();iter.hasNext();) {
                Map.Entry entry=(Map.Entry)iter.next();
                Map item=(Map)entry.getValue();
                itemMap.put(entry.getKey(), itemConvert(item));
            }
        });
        return itemMap;
    }
    public DataType itemConvert(Map itemMap){
        Object dataType = itemMap.getOrDefault("data-type", itemMap.get("dataType"));
       return  DataType.typeOf(dataType);

    }



}
