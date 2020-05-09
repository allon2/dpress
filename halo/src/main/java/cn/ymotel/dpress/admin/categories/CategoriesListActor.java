package cn.ymotel.dpress.admin.categories;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.service.OptionsService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/categories",methods = RequestMethod.GET)
public class CategoriesListActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Autowired
    OptionsService optionsService;
    @Override
    public List Execute(ServletMessage message) throws Throwable {
        Map siteMap=new HashMap();
        siteMap.put("siteid", Utils.getSiteId());
        List list= sqlSession.selectList("categories.qall",siteMap);
        for(int i=0;i<list.size();i++){
            Map tMap=(Map)list.get(i);
            Map tmpMap=new HashMap();
            tmpMap.put("siteid",Utils.getSiteIdFromMessage(message));
            tmpMap.put("postid",tMap.get("id"));
            Map rtnMap=sqlSession.selectOne("post_categories.qpostcountbycategoriesId",tMap);
            tMap.put("postCount",rtnMap.get("ct"));
            String fullpath="/"+optionsService.getOption(Utils.getSiteId(),OptionsService.CATEGORIES_PREFIX,"categories");
            fullpath=fullpath+"/"+tMap.get("slug");
            tMap.put("fullPath",fullpath);
        }
        return list;
    }
}
