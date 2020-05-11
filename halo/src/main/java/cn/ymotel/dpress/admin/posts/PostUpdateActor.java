package cn.ymotel.dpress.admin.posts;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;

//@ActorCfg(urlPatterns = "/api/admin/posts/{postid}",methods = RequestMethod.PUT)
public class PostUpdateActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Object Execute(ServletMessage message) throws Throwable {


        return  new HashMap<>();
    }
}
