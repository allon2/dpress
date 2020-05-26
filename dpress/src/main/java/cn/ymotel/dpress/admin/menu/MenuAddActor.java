package cn.ymotel.dpress.admin.menu;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/menus",methods = RequestMethod.POST)
public class MenuAddActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Map Execute(ServletMessage message) throws Throwable {
        Map map=message.getContext();
        map.put("siteid", Utils.getSiteId());
        map.put("create_time",new java.sql.Timestamp(System.currentTimeMillis()));
        map.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
        if(map.get("target")==null){
            map.put("target","_self");
        }
        if(map.get("parentId")==null){
            map.put("parent_id",0);
        }else{
            map.put("parent_id",map.get("parentId"));
        }
        if(map.get("priority")==null){
            map.put("priority",0);
        }
        sqlSession.insert("menus.i",map);
        return new HashMap();
    }
}
