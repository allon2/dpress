package cn.ymotel.dpress.actor.sitemap;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.service.OptionsService;
import cn.ymotel.largedatabtach.LargeDataBatch;
import cn.ymotel.largedatabtach.ThreadSafeLargeDataBatchHelp;
import cn.ymotel.largedatabtach.support.MybatisBatchDataConsumer;
import cn.ymotel.largedatabtach.support.MybatisResultHandler;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletResponse;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ActorCfg(urlPatterns = "/sitemapurls/{page}.xml",chain = "publicchain",timeout = 1000*60)
public class SiteMapUrlPageActor implements Actor<ServletMessage>, InitializingBean {
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
        Consumer consumer=new Consumer();
        consumer.setSqlSession(sqlSession);

        batchhelp.init(1,20,consumer);
        String archives=optionsService.getArchives(Utils.getSiteId());
        String baseUrl=Utils.getBaseUrl(message);
        for(int i=0;i<10000/100;i++){
            Map data=new HashMap();
            data.put("siteid",Utils.getSiteId());
            data.put("status",0);
            data.put("start",i*100);
            data.put("baseUrl",baseUrl);
            data.put("archives",archives);
            data.put("writer",writer);
            batchhelp.addData(data);
        }

//        {
//            Map data=new HashMap();
//            data.put("siteid",Utils.getSiteId());
//            data.put("status",0);
//            data.put("start",message.getContextData("page",0)*10000);
//           List list= sqlSession.selectList("qlimitbynormal",data);
//            for(int i=0;i<list.size();i++){
//                Map tMap=(Map)list.get(i);
//                String url=baseUrl+"/"+archives+"/"+tMap.get("slug");
//                writer.write(getnewslinkxml(url,(java.util.Date)tMap.get("create_time")));
//                if(i%1000==0) {
//                    writer.flush();
//                }
//            }
//
//
//        }
        batchhelp.end();

        String end="</sitemapindex>\n";
        writer.write(end);
        writer.flush();
        message.getAsyncContext().complete();
        return null;
    }

   private ThreadSafeLargeDataBatchHelp batchhelp=null;

    @Override
    public void afterPropertiesSet() throws Exception {
        batchhelp=new ThreadSafeLargeDataBatchHelp();
        batchhelp.afterPropertiesSet();
    }


}
