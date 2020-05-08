package cn.ymotel.dpress.admin.postcomments;

import cn.ymotel.dactor.ActorUtils;
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
import run.halo.app.model.enums.CommentStatus;
import run.halo.app.model.enums.PostEditorType;
import run.halo.app.model.enums.PostStatus;
import run.halo.app.model.enums.ValueEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/posts/comments",methods = RequestMethod.GET)
public class PageListCommentActor  implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;

    @Override
    public Page Execute(ServletMessage message) throws Throwable {
        int ipage=message.getContextData("page",0);
        int isize=message.getContextData("size",10);
        Pageable pageable= PageRequest
                .of(ipage, isize);
        Map map = new HashMap();
        map.put("siteid", Utils.getSiteId());
        map.put("start", pageable.getOffset());
        map.put("size",isize);
        if(message.getContextData("status")!=null) {
            map.put("status", message.getContextData("status",CommentStatus.class).getValue());
        }
        if(message.getContextData("keyword")!=null) {
            map.put("keyword1", message.getContextData("keyword"));
            map.put("keyword","%"+message.getContextData("keyword")+"%");
        }

        List ls= sqlSession.selectList("comments.qlimit",map);
        for(int i=0;i<ls.size();i++){
            Map tMap=(Map)ls.get(i);
            Integer status=(Integer)tMap.get("status");
            tMap.put("status", ValueEnum.valueToEnum(CommentStatus.class,status));
           Map ttMap=new HashMap();
           ttMap.put("siteid",Utils.getSiteId());
            ttMap.put("id",tMap.get("post_id"));
            tMap.remove("post_id");
            Map postMap= sqlSession.selectOne("posts.q",ttMap);
            if(postMap==null){
                continue;
            }
            tMap.put("post",postMap);
            {
                ;
                postMap.put("editorType",ValueEnum.valueToEnum( PostEditorType.class,(Integer) postMap.get("editorType")).name());
            }
            {

                postMap.put("status", ValueEnum.valueToEnum( PostStatus.class,(Integer) postMap.get("status")).name());
            }

            postMap.put("fullPath","/archives/"+postMap.get("slug"));
        }
        long total= sqlSession.selectOne("comments.qcount",map);
        Page page=new PageImpl(ls,pageable,total);

        return page;
    }
}
