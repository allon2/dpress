package cn.ymotel.dpress.admin.sitemgmt;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.Message;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

@ActorCfg(urlPatterns = "/api/admin/site/update.json")
public class SiteUpdateActor implements Actor {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Object Execute(Message message) throws Throwable {
        sqlSession.update("dpress.usiteinfo",message.getCaseInsensitivegetContext());
        return new HashMap<>();
    }
}