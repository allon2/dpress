package cn.ymotel.dpress.admin.attachments;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.sequence.IdWorker;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import run.halo.app.utils.FilenameUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ActorCfg(urlPatterns = "/api/admin/attachments/{id}",methods = RequestMethod.DELETE)
public class DeleteActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Object Execute(ServletMessage message) throws Throwable {
        Map map=new HashMap();
        map.put("id",message.getContextData("id"));
        map.put("siteid",Utils.getSiteId());
        sqlSession.delete("attachmentfiles.dbyid",map);
        return new HashMap<>();
    }
}
