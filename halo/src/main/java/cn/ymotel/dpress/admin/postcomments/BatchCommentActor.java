package cn.ymotel.dpress.admin.postcomments;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import run.halo.app.model.enums.CommentStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/posts/comments/status/{status}",methods = RequestMethod.PUT)
public class BatchCommentActor  implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Map Execute(ServletMessage message) throws Throwable {
       int value= message.getContextData("status", CommentStatus.class).getValue();
       Map map=new HashMap();
       map.put("status",value);
       map.put("siteid", Utils.getSiteId());
          List ls=  message.getContextList();
          for(int i=0;i<ls.size();i++){
//              Map tMap=(Map)ls.get(i);
              map.put("id",ls.get(i));
              sqlSession.update("comments.ustatus",map);
          }
          return  new HashMap();
    }
}
