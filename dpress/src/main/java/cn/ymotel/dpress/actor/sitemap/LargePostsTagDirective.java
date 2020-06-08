package cn.ymotel.dpress.actor.sitemap;

import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.entity.mapper.PostsMapper;
import cn.ymotel.dpress.service.OptionsService;
import cn.ymotel.largedatabtach.ThreadSafeLargeDataBatchHelp;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import freemarker.core.Environment;
import freemarker.template.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class LargePostsTagDirective  implements TemplateDirectiveModel, InitializingBean {
    @Autowired
    private SqlSession sqlSession;
    @Autowired
    OptionsService optionsService;
    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        Object siteid=null;
        try {
            siteid= ((SimpleNumber)env.getVariable("_siteid")).getAsNumber();
        } catch (java.lang.Throwable e) {
            e.printStackTrace();
        }
        String baseurl=null;
        try{
            baseurl=((SimpleScalar)env.getVariable("_baseurl")).getAsString();
        }catch (Exception e){}
        LargetPostConsumer consumer=new LargetPostConsumer();
        consumer.setSqlSession(sqlSession);
        consumer.setBody(body);
        consumer.setEnv(env);

        long count=getCount(siteid);
        long max=getMaxId(siteid);
        max=max+1;
        long min=getMinId(siteid);
        batchhelp.init(1,20,consumer);
        String archives=optionsService.getArchives(siteid);
        int steps=20000;
        for(long i=min;;){
            Map data=new HashMap();
            data.put("siteid",siteid);
            data.put("status",0);
            data.put("min",i);
            if(i+steps<max) {
                data.put("max", i + steps);
            }else{
                data.put("max", max);
            }
            data.put("baseUrl",baseurl);
            data.put("archives",archives);
//            data.put("Environment",env);
//            data.put("TemplateDirectiveBody",body);
            batchhelp.addData(data);

            i=i+steps;
            if(i>max){
                break;
            }
        }

        batchhelp.end();

//        body.render(env.getOut());

    }
    private ThreadSafeLargeDataBatchHelp batchhelp=null;
    public long getCount(Object siteid){
        Map data=new HashMap();
        data.put("siteid",siteid);
        data.put("status",0);
        Long icount=  sqlSession.selectOne("posts.qcountbynormal",data);
        return icount;
    }
    public long getMaxId(Object siteid){
        Map data=new HashMap();
        data.put("siteid",siteid);
        data.put("status",0);
       return  sqlSession.selectOne("posts.qmaxid",data);
    }
    public long getMinId(Object siteid){
        Map data=new HashMap();
        data.put("siteid",siteid);
        data.put("status",0);
        return  sqlSession.selectOne("posts.qminid",data);
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        batchhelp=new ThreadSafeLargeDataBatchHelp();
        batchhelp.afterPropertiesSet();
    }
}
