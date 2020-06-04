package cn.ymotel.dpress.admin.options;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.Message;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import run.halo.app.model.properties.PropertyEnum;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/options/map_view",methods = RequestMethod.GET)
public class OptionsListActor implements Actor {
    private   Map<String, PropertyEnum> propertyEnumMap= Collections.unmodifiableMap(PropertyEnum.getValuePropertyEnumMap());

    @Autowired
    private SqlSession sqlSession;

    @Override
    public Object Execute(Message message) throws Throwable {

        Map<String, Object> result = new HashMap<>();

        // Add default property
        propertyEnumMap.keySet()
                .stream()
                .forEach(key -> {
                    PropertyEnum propertyEnum = propertyEnumMap.get(key);

                    if (StringUtils.isBlank(propertyEnum.defaultValue())) {
                        return;
                    }

                    result.put(key, PropertyEnum.convertTo(propertyEnum.defaultValue(), propertyEnum));
                });

        Map map=message.getContext();
        map.put("siteid", Utils.getSiteId());
       List list= sqlSession.selectList("options.qall1",map);
//        Map rtnMap=new HashMap();
        for(int i=0;i<list.size();i++){
            Map tMap=(Map)list.get(i);
            String value=(String)tMap.get("option_value");
            if (StringUtils.isBlank(value)) {
                continue;
            }
            String key=(String)tMap.get("option_key");
            PropertyEnum propertyEnum = propertyEnumMap.get(key);
                if(propertyEnum==null){
                    continue;
                }
            result.put(key, PropertyEnum.convertTo(value, propertyEnum));
//            rtnMap.put(key,value);
//            OptionClassConvert.StringConvert(key,rtnMap);
        }
        return result;
    }

}
