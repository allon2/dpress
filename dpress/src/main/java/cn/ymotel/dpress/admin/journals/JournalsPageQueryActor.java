package cn.ymotel.dpress.admin.journals;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMethod;
import run.halo.app.model.enums.JournalType;
import run.halo.app.model.enums.PostStatus;
import run.halo.app.model.enums.ValueEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/journals",methods = RequestMethod.GET)
public class JournalsPageQueryActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Page Execute(ServletMessage message) throws Throwable {
        int ipage=message.getContextData("page",0);
        int isize=message.getContextData("size",10);
        Pageable pageable= PageRequest
                .of(ipage, isize);
        Map map = new HashMap();
        map.put("siteid", Utils.getSiteIdFromMessage(message));
        map.put("start", pageable.getOffset());
        map.put("size",isize);
        if(message.getContextData("type")!=null) {
            map.put("type", JournalType.valueOf(message.getContextData("type")).getValue());
        }
        List ls= sqlSession.selectList("journals.qlimit",map);
        for(int i=0;i<ls.size();i++){
            Map tMap=(Map)ls.get(i);
            Integer type=(Integer)tMap.get("type");
            tMap.put("type",ValueEnum.valueToEnum(JournalType.class,type).name());

        }
        Page page=new PageImpl(ls,pageable,sqlSession.selectOne("journals.qlimitcount",map));
        return page;

    }
}
