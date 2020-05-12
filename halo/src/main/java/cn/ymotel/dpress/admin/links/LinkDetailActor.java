package cn.ymotel.dpress.admin.links;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/links/{id}",methods = RequestMethod.GET)
public class LinkDetailActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Map Execute(ServletMessage message) throws Throwable {
        Map map=message.getContext();
        map.put("siteid",Utils.getSiteId());
        return sqlSession.selectOne("links.qdetail",map);
    }
}
