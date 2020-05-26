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
import run.halo.app.model.enums.PostStatus;
import run.halo.app.model.enums.ValueEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/posts/latest",methods = RequestMethod.GET)
public class PostsLatestActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;

    @Autowired
    private OptionsService optionsService;
    @Override
    public List Execute(ServletMessage message) throws Throwable {
        int ipage=message.getContextData("page",0);
        int isize=message.getContextData("top",5);
        Pageable pageable= PageRequest
                .of(ipage>= 1 ? ipage - 1 : ipage, isize);
        Map map = new HashMap();
        map.put("siteid", Utils.getSiteIdFromMessage(message));
        map.put("start", pageable.getOffset());
        map.put("size",isize);


        List ls= sqlSession.selectList("posts.qadminposts",map);
        for(int i=0;i<ls.size();i++) {
            Map tMap = (Map) ls.get(i);
            Integer status=(Integer)tMap.get("status");
            tMap.put("status", ValueEnum.valueToEnum(PostStatus.class,status).name());
        }

        return ls;
    }
}
