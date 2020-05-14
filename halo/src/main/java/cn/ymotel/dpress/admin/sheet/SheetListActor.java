package cn.ymotel.dpress.admin.sheet;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.Message;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMethod;
import run.halo.app.model.enums.PostStatus;
import run.halo.app.model.enums.ValueEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/sheets",methods = RequestMethod.GET)
public class SheetListActor implements Actor<Message> {
    @Autowired
    private SqlSession sqlSession;

    @Override
    public Page Execute(Message message) throws Throwable {
        Map map=message.getContext();
        Pageable pageable= PageRequest
                .of(message.getContextData("page",0), message.getContextData("size",10));
        map.put("siteid", Utils.getSiteId());
        map.put("start", pageable.getOffset());
        map.put("size",pageable.getPageSize());
       List list= sqlSession.selectList("posts.qsheetlimit",map);
       long count=sqlSession.selectOne("posts.qsheetlimitcount",map);
        for(int i=0;i<list.size();i++){
            Map map1=(Map)list.get(i);
            Map tMap=new HashMap();
            tMap.put("siteid",Utils.getSiteId());
            tMap.put("post_id",map1.get("id"));
            long count1=sqlSession.selectOne("comments.qcountbypostid",tMap);
            map1.put("commentCount",count1);
            map1.put("fullPath","/s/"+map1.get("slug"));
            Integer status=(Integer)map1.get("status");

            map1.put("status", ValueEnum.valueToEnum(PostStatus.class,status).name());
        }
        Page page=new PageImpl(list,pageable,count);
        return page;
    }
}
