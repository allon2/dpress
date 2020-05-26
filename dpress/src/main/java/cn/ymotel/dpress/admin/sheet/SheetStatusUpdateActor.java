package cn.ymotel.dpress.admin.sheet;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.Message;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMethod;
import run.halo.app.model.enums.PostStatus;
import run.halo.app.model.enums.ValueEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/sheets/{id}/{status}",methods = RequestMethod.PUT)
public class SheetStatusUpdateActor implements Actor<Message> {
    @Autowired
    private SqlSession sqlSession;

    @Override
    public Map Execute(Message message) throws Throwable {
        Map map=new HashMap();
        map.put("siteid", Utils.getSiteId());
        int value= message.getContextData("status", PostStatus.class).getValue();
        map.put("status",value);
        map.put("id",message.getContextData("id"));
        sqlSession.update("posts.ustatusbyid",map);
        return  new HashMap();
    }
}
