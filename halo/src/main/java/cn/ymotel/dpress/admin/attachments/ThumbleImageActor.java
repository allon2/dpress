package cn.ymotel.dpress.admin.attachments;

import cn.ymotel.dactor.Constants;
import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.core.MessageThreadLocal;
import cn.ymotel.dactor.message.Message;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * 因为图片中已经包含siteid，可以对siteid进行match，后面做
 */
@ActorCfg(urlPatterns = "/upload/**",view = "img:")
public class ThumbleImageActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Object Execute(ServletMessage message) throws Throwable {
        String path=message.getControlData(Constants.EXTRACTPATH);
        Map map=new HashMap<>();
        map.put("path","upload/"+path);
        map.put("siteid", Utils.getSiteId());
        Map attachMap=sqlSession.selectOne("attachmentfiles.qdetail",map);
        return attachMap.get("content");
    }

}
