package cn.ymotel.dpress.admin.sitemgmt;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.Message;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.service.OptionsService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/site/list.json")
public class SiteListActor implements Actor {
    @Autowired
    private SqlSession sqlSession;

    @Autowired
    private OptionsService optionsService;
    @Override
    public Object Execute(Message message) throws Throwable {
        List ls=sqlSession.selectList("dpress.qallsite");

        for(int i=0;i<ls.size();i++){
             Map map=(Map)ls.get(i);
           String title= optionsService.getOption(map.get("id"),"blog_title","");
           String url= optionsService.getOption(map.get("id"),"blog_url","");
            map.put("url",url);
            map.put("title",title);
            map.put("parentId",0);
             map.put("children",null);
        }
        return ls;
    }
}
