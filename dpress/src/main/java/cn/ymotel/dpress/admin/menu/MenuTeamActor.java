package cn.ymotel.dpress.admin.menu;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/menus/teams",methods = RequestMethod.GET)
public class MenuTeamActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public List Execute(ServletMessage message) throws Throwable {
        Map map=message.getContext();
        map.put("siteid", Utils.getSiteId());

       List ls=sqlSession.selectList("menus.qteams",map);
        return ls;
    }

}
