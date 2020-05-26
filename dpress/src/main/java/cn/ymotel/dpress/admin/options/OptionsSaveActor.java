package cn.ymotel.dpress.admin.options;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.Message;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@ActorCfg(urlPatterns = "/api/admin/options/map_view/saving",methods = RequestMethod.POST)
public class OptionsSaveActor implements Actor {
    @Autowired
    private SqlSession sqlSession;

    @Override
    public Object Execute(Message message) throws Throwable {
        Map map=message.getContext();

        map.forEach((key, value) -> {
            if(key.toString().startsWith("_")){
                return;
            }
            if(key.toString().equals("birthday")){
                return;
            }
            if(key.toString().equals("is_installed")){
                return;
            }
            if(key.toString().equals("installthemes")){
                return;
            }
            if(key.toString().equals("theme")){
                return;
            }
            Map params=new HashMap();
            params.put("siteid", Utils.getSiteId());
            params.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
            params.put("option_value", value);
            params.put("option_key", key);
            int update=sqlSession.update("options.uoption",params);
            if(update==0){
                params.put("type",0);
                params.put("create_time",new java.sql.Timestamp(System.currentTimeMillis()));
                sqlSession.insert("options.ioption",params);
            }
        });


        return new HashMap<>();
    }

}
