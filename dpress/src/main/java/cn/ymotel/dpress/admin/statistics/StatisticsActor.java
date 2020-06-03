package cn.ymotel.dpress.admin.statistics;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.service.OptionsService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@ActorCfg(urlPatterns = "/api/admin/statistics",methods = RequestMethod.GET)
public class StatisticsActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;

    @Autowired
    private   OptionsService optionsService;
    @Override
    public Map Execute(ServletMessage message) throws Throwable {
        Map rtnMap=new HashMap();
        Map map=new java.util.concurrent.ConcurrentHashMap<>();
        map.put("siteid", Utils.getSiteId());
        CompletableFuture future1= CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                rtnMap.put("postCount",sqlSession.selectOne("posts.qcount",map));
            }
        });
        CompletableFuture future2= CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                rtnMap.put("commentCount",sqlSession.selectOne("comments.qcount",map));
            }
        });
        CompletableFuture future3=CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                rtnMap.put("categoryCount",sqlSession.selectOne("categories.qcount",map));
            }
        });
        CompletableFuture future4= CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                rtnMap.put("attachmentCount",sqlSession.selectOne("attachmentfiles.qcount",map));
            }
        });
        CompletableFuture future5=CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                rtnMap.put("tagCount",sqlSession.selectOne("tags.qcount",map));
            }
        });
        CompletableFuture future6=CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                rtnMap.put("journalCount",sqlSession.selectOne("journals.qcount",map));
            }
        });
        CompletableFuture future7= CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                rtnMap.put("linkCount",sqlSession.selectOne("links.qcount",map));
            }
        });

        {

            map.put("type",0);
            long count=sqlSession.selectOne("posts.qsumvisit", map);

            map.put("type",1);
            count=count+(Long)sqlSession.selectOne("posts.qsumvisit", map);
            rtnMap.put("visitCount", count);
        }
        {            map.put("type",0);
            long count= sqlSession.selectOne("posts.qsumlikes", map);
                        map.put("type",1);
                count=count+(Long)sqlSession.selectOne("posts.qsumlikes", map);

            rtnMap.put("likeCount",count);
        }

       String birthday= optionsService.getOption(Utils.getSiteId(),"birthday",null);
       Long lbirthday=Long.parseLong(birthday);
        long days = (System.currentTimeMillis() - lbirthday) / (1000 * 24 * 3600);
        rtnMap.put("birthday",lbirthday);
        rtnMap.put("establishDays",days);
        // Wait until they are all done
        CompletableFuture.allOf(future1,future2,future3,future4,future5,future6,future7).join();


        return rtnMap;
    }
}
