package cn.ymotel.dpress.admin.sitemgmt;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.Message;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/site/list.json")
public class SiteListActor implements Actor {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Object Execute(Message message) throws Throwable {
        List ls=sqlSession.selectList("dpress.qallsite");

        for(int i=0;i<ls.size();i++){
             Map map=(Map)ls.get(i);
             map.put("parentId",0);
             map.put("children",null);
        }
        return ls;
    }
}
