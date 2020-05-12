package cn.ymotel.dpress.admin.sheet;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/sheets/independent",methods = RequestMethod.GET)
public class IndependentActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public List Execute(ServletMessage message) throws Throwable {
        List ls=new ArrayList();
        {
            Map map=new HashMap();
            map.put("id",1);
            map.put("title","友情链接");
            map.put("fullPath","/links");
            map.put("routeName","LinkList");
            map.put("available",true);
            ls.add(map);
        }
        {
            Map map=new HashMap();
            map.put("id",2);
            map.put("title","图库页面");
            map.put("fullPath","/photos");
            map.put("routeName","PhotoList");
            map.put("available",true);
            ls.add(map);
        }
        {
            Map map=new HashMap();
            map.put("id",3);
            map.put("title","日志页面");
            map.put("fullPath","/journals");
            map.put("routeName","JournalList");
            map.put("available",false);
            ls.add(map);
        }

        return ls;
    }
}
