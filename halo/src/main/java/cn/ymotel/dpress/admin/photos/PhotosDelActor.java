package cn.ymotel.dpress.admin.photos;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/photos/{id}",methods = RequestMethod.DELETE)
public class PhotosDelActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Map Execute(ServletMessage message) throws Throwable {

        Map map = message.getContext();
        map.put("siteid", Utils.getSiteId());

        sqlSession.delete("photos.d",map);


        return new HashMap();
    }

}
