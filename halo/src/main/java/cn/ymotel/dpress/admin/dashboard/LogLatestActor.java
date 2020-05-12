package cn.ymotel.dpress.admin.dashboard;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.service.OptionsService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMethod;
import run.halo.app.model.enums.LogType;
import run.halo.app.model.enums.PostStatus;
import run.halo.app.model.enums.ValueEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/logs/latest",methods = RequestMethod.GET)
public class LogLatestActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;

    @Override
    public List Execute(ServletMessage message) throws Throwable {
        int ipage=message.getContextData("page",0);
        int isize=message.getContextData("top",5);
        Pageable pageable= PageRequest
                .of(ipage>= 1 ? ipage - 1 : ipage, isize);
        Map map = new HashMap();
        map.put("siteid", Utils.getSiteIdFromMessage(message));
        map.put("size",isize);

        List ls= sqlSession.selectList("logs.qlogtop",map);
        for(int i=0;i<ls.size();i++){
           Map tMap=(Map) ls.get(i);
           Integer type=(Integer) tMap.get("type");
            tMap.put("type", ValueEnum.valueToEnum(LogType.class,type).name());
        }

        return ls;
    }
}
