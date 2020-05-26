package cn.ymotel.dpress.admin.options;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.Message;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import run.halo.app.model.properties.PropertyEnum;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/options/map_view/keys",methods = RequestMethod.POST)
public class OptionsKeysListActor implements Actor {
    @Autowired
    private SqlSession sqlSession;
    private  Map<String, PropertyEnum> propertyEnumMap= Collections.unmodifiableMap(PropertyEnum.getValuePropertyEnumMap());

    @Override
    public Object Execute(Message message) throws Throwable {
        Map tmpMap = new HashMap();

        {
            Map map = new HashMap();
            map.put("siteid", Utils.getSiteId());
            List list = sqlSession.selectList("options.qall1", map);
            for (int i = 0; i < list.size(); i++) {
                Map tMap = (Map) list.get(i);
                String value = (String) tMap.get("option_value");
                String key = (String) tMap.get("option_key");
                tmpMap.put(key, value);
//                OptionClassConvert.StringConvert(key, tmpMap);
            }
        }
       List list= message.getContextList();
        Map rtnMap=new HashMap();
        for(int i=0;i<list.size();i++){
            String key=(String)list.get(i);
            Object tmpvalue=tmpMap.get(key);
            Object value=null;
            if(tmpvalue==null){
                value=propertyEnumMap.get(key).defaultValue();
            }else{
                value=tmpMap.get(key);
            }
            value=PropertyEnum.convertTo(value.toString(),propertyEnumMap.get(key));
            rtnMap.put(key,value);
        }
        return rtnMap;
    }

}
