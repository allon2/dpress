package cn.ymotel.dpress.admin.sitemgmt;

import cn.hutool.core.collection.CollUtil;
import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.Message;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.entity.mapper.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/site/delete.json")
public class SiteDleteActor implements Actor {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Object Execute(Message message) throws Throwable {
        sqlSession.delete("dpress.dsiteinfo",message.getCaseInsensitivegetContext());
        Map params=new HashMap();
        params.put("siteid",message.getContextData("id"));
        sqlSession.getMapper(PostsMapper.class).deleteByMap(params);
        sqlSession.getMapper(DpressTemplateMapper.class).deleteByMap(params);
        sqlSession.getMapper(PostTagsMapper.class).deleteByMap(params);
        sqlSession.getMapper(PostCategoriesMapper.class).deleteByMap(params);
        sqlSession.getMapper(MenusMapper.class).deleteByMap(params);
        sqlSession.getMapper(ThemeSettingsMapper.class).deleteByMap(params);
        sqlSession.getMapper(TagsMapper.class).deleteByMap(params);
        sqlSession.getMapper(PhotosMapper.class).deleteByMap(params);
        sqlSession.getMapper(OptionsMapper.class).deleteByMap(params);
        sqlSession.getMapper(MetasMapper.class).deleteByMap(params);
        sqlSession.getMapper(JournalsMapper.class).deleteByMap(params);
        sqlSession.getMapper(CommentBlackListMapper.class).deleteByMap(params);
        sqlSession.getMapper(CommentsMapper.class).deleteByMap(params);
        sqlSession.getMapper(CategoriesMapper.class).deleteByMap(params);
        sqlSession.getMapper(AttachmentsMapper.class).deleteByMap(params);

        return new HashMap<>();
    }
}
