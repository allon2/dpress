package cn.ymotel.dpress.actor.sitemap;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.service.OptionsService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.support.BindingAwareModelMap;

import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ActorCfg(urlPatterns = "/sitemap.xml",chain = "publicchain",timeout = 1000*60)
public class SiteMapXmlActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Autowired
    OptionsService optionsService;

    @Override
    public <E> E HandleMessage(ServletMessage message) throws Throwable {
        ServletResponse response=message.getAsyncContext().getResponse();
        response.setContentType("application/xml;charset=UTF-8");
        PrintWriter writer=response.getWriter();
        String head="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                " <sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n" ;
        writer.write(head);
        writer.flush();
        {
            Map data=new HashMap();
            data.put("siteid",Utils.getSiteId());
            data.put("status",0);
            Long icount=  sqlSession.selectOne("posts.qcountbynormal",data);
            long page=0;
            page=icount/10000;
            if(icount%10000!=0){
                page++;
            }
            StringBuilder sb=new StringBuilder();
            java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyy-MM-dd");
            String baseUrl=Utils.getBaseUrl(message);

            for(int i=0;i<page;i++){
                sb.append(" <sitemap>\n" +
                        " <loc>"+baseUrl+"/sitemapurls/"+i+".xml</loc>\n" +
                        "<lastmod>"+sdf.format(new java.util.Date())+"</lastmod>\n" +
                        "</sitemap>\n");
            }
            writer.write(sb.toString());

        }
        String end="</sitemapindex>\n";
        writer.write(end);
        writer.flush();
        message.getAsyncContext().complete();
        return null;
    }

    //    @Override
//    public Object Execute(ServletMessage message) throws Throwable {
//        String baseUrl=Utils.getBaseUrl(message);
//        BindingAwareModelMap model=new BindingAwareModelMap();
//        ViewData viewData=new ViewData();
//        Map paramsMap=new HashMap();
//        paramsMap.put("siteid", Utils.getSiteId());
//       List posts= sqlSession.selectList("posts.qall",paramsMap);
//       for(int i=0;i<posts.size();i++){
//           Map tMap=(Map)posts.get(i);
//           tMap.put("fullPath",baseUrl+"/"+optionsService.getArchives(Utils.getSiteId())+"/"+tMap.get("slug"));
//       }
//        model.addAttribute("posts", posts);
//        viewData.setData(model);
//        viewData.setContentType("application/xml;charset=UTF-8");
//        viewData.setViewName("common/web/sitemap_xml");
//        return viewData;
//    }

}
