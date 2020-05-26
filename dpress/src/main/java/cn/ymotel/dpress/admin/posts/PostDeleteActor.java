package cn.ymotel.dpress.admin.posts;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import run.halo.app.model.enums.PostStatus;

import java.util.HashMap;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/posts/{id}",methods = RequestMethod.DELETE)
public class PostDeleteActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Map Execute(ServletMessage message) throws Throwable {
        Map map=new HashMap();
        map.put("siteid", Utils.getSiteId());

        map.put("id",message.getContextData("id"));
        sqlSession.delete("posts.d",map);
        return  new HashMap();
    }
}
