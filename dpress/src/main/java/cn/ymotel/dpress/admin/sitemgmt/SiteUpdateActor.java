package cn.ymotel.dpress.admin.sitemgmt;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.Message;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.entity.mapper.OptionsMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/site/update.json")
public class SiteUpdateActor implements Actor {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Object Execute(Message message) throws Throwable {
        sqlSession.update("dpress.usiteinfo",message.getCaseInsensitivegetContext());

        Map map=new HashMap<>();
        map.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
        map.put("siteid",message.getContextData("id"));
        map.put("option_value",message.getContextData("url"));
        map.put("option_key","blog_url");
        sqlSession.update("options.uoption",map);
        map.put("option_value",message.getContextData("title"));
        map.put("option_key","blog_title");
        sqlSession.update("options.uoption",map);
         return new HashMap<>();
    }
}
