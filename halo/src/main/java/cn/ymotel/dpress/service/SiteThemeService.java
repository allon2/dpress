package cn.ymotel.dpress.service;

import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.actor.OptionsService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import run.halo.app.model.enums.DataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Component
public class SiteThemeService {
    @Autowired
    private SqlSession sqlSession;
    @Autowired
    private OptionsService optionsService;
    public String getActiveThemeName(Object siteid){
       return  optionsService.getOption(siteid,"theme",null);
    }
    public Map getThemeInfo(Object siteid,String theme,String path){
        Map map=new HashMap();
        map.put("path",path);
        map.put("theme",theme);
        map.put("siteid",siteid);
        Map  tmpthemeMap= sqlSession.selectOne("dpress.qtemplate",map);
        return tmpthemeMap;

    }
    public static boolean istext(String path){
        path=path.toLowerCase();
        if(path.endsWith("ftl")){
            return true;
        }
        if(path.endsWith("js")){
            return true;
        }
        if(path.endsWith("css")){
            return true;
        }
        if(path.endsWith("md")){
            return true;
        }
        if(path.endsWith("yaml")){
            return true;
        }
        return false;
    }
    public Object getContent(Object siteid,String theme,String path){
       Map map= getThemeInfo(siteid,theme,path);
       if(map==null){
           return null;
       }
       if(istext(path)){
          return   map.get("content");
       }
       return map.get("bcontent");

    }
    public Map getThemeInfo(Object siteid,String theme){
         Map map= getThemeInfo(siteid,theme,"theme.yaml");
        if(map==null||map.isEmpty()){
            return null;
        }
        String content=(String) map.get("content");
        Yaml yaml = new Yaml();
        Map yamlmap =  yaml.load(content);
        return  yamlmap;
    }

    public Map<String , String> getSettingWithValue(Object siteid,String themeId){
       Map<String,DataType> settingDataType= getSettingDataType(siteid,themeId);
        Map<String,String>  settingWithDefaultValue=getSettingWithDefaultValue(siteid,themeId);


        Map tMap=new HashMap();
        tMap.put("siteid",siteid);
        tMap.put("theme_id",themeId);
        List<Map> ls= sqlSession.selectList("theme_settings.qall",tMap);
        Map rtnMap=new HashMap();
        settingWithDefaultValue.forEach(new BiConsumer<String, Object>() {
            @Override
            public void accept(String key, Object value) {
              rtnMap.put(key,value);
            }
        });




        ls.forEach(map2 -> {

            rtnMap.put(map2.get("setting_key"),settingDataType.get(map2.get("setting_key")).convertTo(map2.get("setting_value")));
        });


        return rtnMap;
    }
    public Map<String,String> getSettingWithDefaultValue(Object siteid,String themeId){
        Map map=new HashMap();
        map.put("path","settings.yaml");
        map.put("theme",themeId);
        map.put("siteid", siteid);
        Map  tmpthemeMap= sqlSession.selectOne("dpress.qtemplate",map);
        Map itemMap=new HashMap();

        if(tmpthemeMap!=null){
            String content=(String) tmpthemeMap.get("content");
            Yaml yaml = new Yaml();
            Map yamlmap =  yaml.load(content);
            yamlmap.forEach((key, value) -> {
                Map items=(Map)((Map)value).get("items");
                for(java.util.Iterator iter=items.entrySet().iterator();iter.hasNext();) {
                    Map.Entry entry=(Map.Entry)iter.next();
                    Map item=(Map)entry.getValue();
                    Object defaultValue=item.get("default");
                    if(defaultValue!=null) {
                        itemMap.put(entry.getKey(), defaultValue);
                    }
                }
            });
        }
        return itemMap;
    }
    public Map<String , DataType> getSettingDataType(Object siteid,String themeId) {
        Map map=new HashMap();
        map.put("path","settings.yaml");
        map.put("theme",themeId);
        map.put("siteid", siteid);
        Map  tmpthemeMap= sqlSession.selectOne("dpress.qtemplate",map);
        Map itemMap=new HashMap();

        if(tmpthemeMap!=null){
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
        }
        return itemMap;
    }
        private DataType itemConvert(Map itemMap){
        Object dataType = itemMap.getOrDefault("data-type", itemMap.get("dataType"));
        return  DataType.typeOf(dataType);

    }

}
