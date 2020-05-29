package cn.ymotel.dpress.admin.menu;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/menus/{id}",methods = RequestMethod.DELETE)
public class MenuDelActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Map Execute(ServletMessage message) throws Throwable {
        Map map=message.getContext();
        map.put("siteid", Utils.getSiteId());
        map.put("parent_id", message.getContextData("id",Integer.class));
        sqlSession.delete("menus.dbyparentid",map);
        sqlSession.delete("menus.d",map);
        return new HashMap();
    }
}
