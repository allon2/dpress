package cn.ymotel.dpress.admin.journals;

import cn.hutool.core.util.URLUtil;
import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.service.OptionsService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMethod;
import run.halo.app.model.enums.CommentStatus;

import java.util.HashMap;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/journals/comments/{commentId}",methods = RequestMethod.DELETE)
public class CommentDelActor implements Actor<ServletMessage> {

    @Autowired
    private SqlSession sqlSession;
    @Override
    public Map Execute(ServletMessage message) throws Throwable {
        Map map=new HashMap<>();
        map.put("siteid", Utils.getSiteId());
        map.put("id",message.getContextData("commentId"));
        sqlSession.delete("comments.d",map);
        map.put("parent_id",message.getContextData("commentId"));
        sqlSession.delete("comments.dbyparentId",map);
        return new HashMap();
    }
}
