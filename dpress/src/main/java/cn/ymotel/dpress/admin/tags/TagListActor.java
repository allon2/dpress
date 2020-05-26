package cn.ymotel.dpress.admin.tags;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/tags",methods = RequestMethod.GET)
public class TagListActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public List Execute(ServletMessage message) throws Throwable {
        Map map=message.getContext();

        map.put("siteid", Utils.getSiteId());
       List ls= sqlSession.selectList("tags.qall",map);
       for(int i=0;i<ls.size();i++){
            Map tMap=(Map)ls.get(i);
            Map ttMap=new HashMap();
           ttMap.put("siteid",Utils.getSiteId());
           ttMap.put("tag_id",tMap.get("id"));
            Long count=sqlSession.selectOne("posttag.qcountbytagid",ttMap);
            tMap.put("postCount",count);
       }
        return ls;
    }
}
