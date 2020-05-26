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
import java.util.List;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/posts/status/{status}",methods = RequestMethod.PUT)
public class BatchPostStatusActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Map Execute(ServletMessage message) throws Throwable {
        Map map=new HashMap();
        map.put("siteid", Utils.getSiteId());
        int value= message.getContextData("status", PostStatus.class).getValue();
        map.put("status",value);
        List ls=message.getContextList();
        for(int i=0;i<ls.size();i++) {
            map.put("id", ls.get(i));
            sqlSession.update("posts.ustatusbyid",map);
        }
        return  new HashMap();
    }
}
