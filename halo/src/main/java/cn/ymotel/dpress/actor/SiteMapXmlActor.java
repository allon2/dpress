package cn.ymotel.dpress.actor;

import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.service.OptionsService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.support.BindingAwareModelMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ActorCfg(urlPatterns = "/sitemap.xml",chain = "publicchain")
public class SiteMapXmlActor extends  FreemarkerActor {
    @Autowired
    private SqlSession sqlSession;
    @Autowired
    OptionsService optionsService;
    @Override
    public Object Execute(ServletMessage message) throws Throwable {
        String baseUrl=Utils.getBaseUrl(message);
        BindingAwareModelMap model=new BindingAwareModelMap();
        ViewData viewData=new ViewData();
        Map paramsMap=new HashMap();
        paramsMap.put("siteid", Utils.getSiteId());
       List posts= sqlSession.selectList("posts.qall",paramsMap);
       for(int i=0;i<posts.size();i++){
           Map tMap=(Map)posts.get(i);
           tMap.put("fullPath",baseUrl+"/"+optionsService.getArchives(Utils.getSiteId())+"/"+tMap.get("slug"));
       }
        model.addAttribute("posts", posts);
        viewData.setData(model);
        viewData.setContentType("\"application/xml;charset=UTF-8");
        viewData.setViewName("common/web/sitemap_xml");
        return viewData;
    }
}
