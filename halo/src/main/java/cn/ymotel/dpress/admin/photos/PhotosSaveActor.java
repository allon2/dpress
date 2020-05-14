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

@ActorCfg(urlPatterns = "/api/admin/photos/{id}",methods = RequestMethod.PUT)
public class PhotosSaveActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Map Execute(ServletMessage message) throws Throwable {

        Map map = message.getContext();
        map.put("siteid", Utils.getSiteId());
        map.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
        Object takeTime=map.get("takeTime");
        if(takeTime!=null ) {
            if(takeTime instanceof String) {
                map.put("take_time", dealDateFormat((String) map.get("takeTime")));
            }
            if(takeTime instanceof Long){
                map.put("take_time",new java.sql.Timestamp((Long)takeTime));
            }
        }
        sqlSession.update("photos.u",map);


        return new HashMap();
    }
    public static java.util.Date dealDateFormat(String date ) {
        date = date.replace("Z", " UTC");
        System.out.println(date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        try {
            Date d = format.parse(date);
            return new java.sql.Timestamp(d.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
