package cn.ymotel.dpress.admin.photos;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/photos",methods = RequestMethod.GET)
public class PhotosPageActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Page Execute(ServletMessage message) throws Throwable {
        int ipage=message.getContextData("page",0);
        int isize=message.getContextData("size",18);
        Pageable pageable= PageRequest
                .of(ipage, isize);
        Map map = new HashMap();
        map.put("siteid", Utils.getSiteId());
        map.put("start", pageable.getOffset());
        map.put("size",isize);
        map.put("team",message.getContextData("team"));
        if(message.getContext().get("keyword")!=null){
            map.put("keyword","%"+message.getContext().get("keyword")+"%");
        }
        List ls= sqlSession.selectList("photos.qlimit",map);

        long total= sqlSession.selectOne("photos.qlimitcount",map);
        Page page=new PageImpl(ls,pageable,total);

        return page;
    }
}
