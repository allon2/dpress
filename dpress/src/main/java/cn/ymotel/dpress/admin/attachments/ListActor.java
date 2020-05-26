package cn.ymotel.dpress.admin.attachments;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.sequence.IdWorker;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMethod;
import run.halo.app.utils.FilenameUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ActorCfg(urlPatterns = "/api/admin/attachments",methods = RequestMethod.GET)
public class ListActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Object Execute(ServletMessage message) throws Throwable {
        String currentPage=message.getContextData("page","0");
        int ipage=Integer.parseInt(currentPage);
        String size=message.getContextData("size","18");
        int isize=Integer.parseInt(size);
        Pageable pageable= PageRequest
                .of(ipage>= 1 ? ipage - 1 : ipage, isize);
        Map map = new HashMap();
        map.put("siteid", Utils.getSiteId());
        map.put("start", pageable.getOffset());
        map.put("size",isize);
        if(message.getContextData("mediaType")!=null) {
            map.put("media_type", message.getContextData("mediaType"));
        }
        if(message.getContextData("attachmentType")!=null) {
            String attachmentType = message.getContextData("attachmentType");
            if (attachmentType.equals("LOCAL")) {
                map.put("type", "0");
            }
        }
        String keyword=message.getContextData("keyword");
        if(keyword==null||keyword.trim().equals("")){

        }else{
            map.put("keyword","%"+keyword+"%");
        }
       List ls=  sqlSession.selectList("attachmentfiles.qlimit",map);
        Map totalMap=sqlSession.selectOne("attachmentfiles.qtotal",map);
        Page page=new PageImpl(ls,pageable,(Long)totalMap.get("ct"));
        return  page;
    }
}
