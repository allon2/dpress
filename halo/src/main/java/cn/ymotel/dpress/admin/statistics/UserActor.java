package cn.ymotel.dpress.admin.statistics;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.Message;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.service.OptionsService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/statistics/user",methods = RequestMethod.GET)
public class UserActor implements Actor<Message> {
    @Autowired
    private SqlSession sqlSession;

    @Autowired
    OptionsService optionsService;
    @Override
    public Map Execute(Message message) throws Throwable {
        Map rtnMap=new HashMap();
        rtnMap.put("postCount",sqlSession.selectOne("statistics.postCount"));
        {
            long count=0;
            count=count+(Long)sqlSession.selectOne("statistics.postCommentCount");
            count=count+(Long)sqlSession.selectOne("statistics.sheetCommentCount");
            count=count+(Long)sqlSession.selectOne("statistics.journalCommentCount");

            rtnMap.put("commentCount",  count);
        }
        rtnMap.put("categoryCount",  sqlSession.selectOne("statistics.categoryCount"));
        rtnMap.put("attachmentCount",  sqlSession.selectOne("statistics.attachmentCount"));
        rtnMap.put("tagCount",  sqlSession.selectOne("statistics.tagCount"));
        rtnMap.put("journalCount",  sqlSession.selectOne("statistics.journalCount"));
        rtnMap.put("linkCount",  sqlSession.selectOne("statistics.linkCount"));
        {
            long count=0;
            count=count+(Long)sqlSession.selectOne("statistics.postvisitscount");
            count=count+(Long)sqlSession.selectOne("statistics.sheetvisitscount");

            rtnMap.put("visitCount",  count);
        }
        {
            long count=0;
            count=count+(Long)sqlSession.selectOne("statistics.sheetlikescount");
            count=count+(Long)sqlSession.selectOne("statistics.postlikescount");

            rtnMap.put("likeCount",  count);
        }
        {
            String  sbirthday=optionsService.getOption(Utils.getSiteId(), "birthday", null);
            long birthday=Long.parseLong(sbirthday);
            long days = (System.currentTimeMillis() - birthday) / (1000 * 24 * 3600);
            rtnMap.put("birthday",birthday );
            rtnMap.put("establishDays", days);

        }
        rtnMap.put("user",sqlSession.selectOne("statistics.quser"));

        return rtnMap;
    }
}
