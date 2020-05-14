package cn.ymotel.dpress.admin.photos;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@ActorCfg(urlPatterns = "/api/admin/photos",methods = RequestMethod.POST)
public class PhotosAddActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Map Execute(ServletMessage message) throws Throwable {

        Map map = message.getContext();
        map.put("siteid", Utils.getSiteId());
        map.put("create_time",new java.sql.Timestamp(System.currentTimeMillis()));
        map.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
        if(map.get("takeTime")!=null) {

            map.put("take_time", dealDateFormat((String) map.get("takeTime")));
        }
        sqlSession.insert("photos.i",map);


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
