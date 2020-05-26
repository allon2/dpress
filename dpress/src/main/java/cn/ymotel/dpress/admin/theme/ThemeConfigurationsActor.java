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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@ActorCfg(urlPatterns = "/api/admin/themes/{themeId}/configurations",methods = RequestMethod.GET)
public class ThemeConfigurationsActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Object Execute(ServletMessage message) throws Throwable {
        Map map=new HashMap();
        map.put("path","settings.yaml");
        map.put("theme",message.getContextData("themeId"));
        map.put("siteid",Utils.getSiteIdFromMessage(message));
        Map  tmpthemeMap= sqlSession.selectOne("dpress.qtemplate",map);
        if(tmpthemeMap==null){

        }
        String content=(String) tmpthemeMap.get("content");
        Yaml yaml = new Yaml();
        Map yamlmap =  yaml.load(content);
        List groupList=new ArrayList();

        yamlmap.forEach((o, o2) -> {
            Map tMap=new HashMap();
            tMap.put("name",o);
            tMap.put("label",((Map)o2).get("label"));
            Map items=(Map)((Map)o2).get("items");
            List itemsList=new ArrayList();
            for(java.util.Iterator iter=items.entrySet().iterator();iter.hasNext();){
                Map.Entry entry=(Map.Entry)iter.next();
                Map item=(Map)entry.getValue();
                itemConvert(item);
                itemsList.add(item);
            }
            tMap.put("items",items);
            groupList.add(tMap);
        });
//        yamlmap.forEach(new BiConsumer() {
//            @Override
//            public void accept(Object o, Object o2) {
//                if(o.equals("items")){
//                    List items=(List)o2;
//                    for(int i=0;i<items.size();i++){
//                      itemConvert((Map)items.get(i));
//
//                    }
//                }
//            }
//        });

        return groupList;

    }
    public Map itemConvert(Map itemMap){
        Object dataType = itemMap.getOrDefault("data-type", itemMap.get("dataType"));
        itemMap.put("dataType",DataType.typeOf(dataType));
        itemMap.put("type", InputType.typeOf(itemMap.get("type")));
        itemMap.put("defaultValue",itemMap.get("default"));
        return null;
    }

}
