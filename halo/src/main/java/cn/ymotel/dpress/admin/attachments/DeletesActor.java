package cn.ymotel.dpress.admin.attachments;

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

@ActorCfg(urlPatterns = "/api/admin/attachments",methods = RequestMethod.DELETE)
public class DeletesActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Object Execute(ServletMessage message) throws Throwable {
       List list= message.getContextList();
       for(int i=0;i<list.size();i++){
           Map map=new HashMap();
           map.put("id",list.get(i));
           map.put("siteid",Utils.getSiteId());
           sqlSession.delete("attachmentfiles.dbyid",map);
       }

        return new HashMap<>();
    }
}
