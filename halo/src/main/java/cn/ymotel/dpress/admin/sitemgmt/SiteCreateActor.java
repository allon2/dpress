package cn.ymotel.dpress.admin.sitemgmt;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.Message;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

@ActorCfg(urlPatterns = "/api/admin/site/create.json")
public class SiteCreateActor implements Actor {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Object Execute(Message message) throws Throwable {
        message.getContext().put("id",IdWorker.getId());
        sqlSession.insert("dpress.isiteinfo",message.getCaseInsensitivegetContext());
        return new HashMap<>();
    }
}
