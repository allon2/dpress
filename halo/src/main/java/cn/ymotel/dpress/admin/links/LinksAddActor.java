package cn.ymotel.dpress.admin.links;

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

@ActorCfg(urlPatterns = "/api/admin/links",methods = RequestMethod.POST)
public class LinksAddActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Map Execute(ServletMessage message) throws Throwable {
        Map map=message.getContext();
        map.put("create_time",new java.sql.Timestamp(System.currentTimeMillis()));
        map.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
        map.put("siteid", Utils.getSiteId());
        if(map.get("priority")==null){
            map.put("priority",0);
        }
        sqlSession.insert("links.i",map);
        return  new HashMap();
    }
}
