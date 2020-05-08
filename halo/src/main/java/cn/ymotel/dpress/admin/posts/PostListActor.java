package cn.ymotel.dpress.admin.posts;


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
import run.halo.app.model.enums.PostStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/posts")
public class PostListActor  implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Page Execute(ServletMessage message) throws Throwable {
        String currentPage=message.getContextData("page","0");
        int ipage=Integer.parseInt(currentPage);
        String size=message.getContextData("size","10");
        int isize=Integer.parseInt(size);
        Pageable pageable= PageRequest
                .of(ipage>= 1 ? ipage - 1 : ipage, isize);
        Map map = new HashMap();
        map.put("siteid", Utils.getSiteIdFromMessage(message));
        map.put("start", pageable.getOffset());
        map.put("size",isize);
        if(message.getContextData("status")!=null) {
            map.put("status", PostStatus.valueOf(message.getContextData("status")).getValue());
        }
        if(message.getContextData("keyword")!=null) {
            map.put("keyword1", message.getContextData("keyword"));
            map.put("keyword", "%" + message.getContextData("keyword") + "%");
        }
        map.put("categoryId",message.getContextData("categoryId"));
       List ls= sqlSession.selectList("posts.qadminposts",map);
        Map ctMap= sqlSession.selectOne("posts.qadmincountposts",map);
        long total=Long.parseLong(ctMap.get("ct").toString());
        Page page=new PageImpl(ls,pageable,total);

        return page;
    }
    public static void main(String[] args){
        System.out.println(PostStatus.valueOf("PUBLISHED").getValue());
    }
}
