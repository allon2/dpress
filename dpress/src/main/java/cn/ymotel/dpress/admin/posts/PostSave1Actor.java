package cn.ymotel.dpress.admin.posts;


import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import run.halo.app.exception.AlreadyExistsException;
import run.halo.app.model.enums.PostEditorType;
import run.halo.app.model.enums.PostStatus;
import run.halo.app.utils.MarkdownUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
//@ActorCfg(urlPatterns = "/api/admin/posts")

@ActorCfg(urlPatterns = "/api/admin/posts/{id}",methods = RequestMethod.PUT)
public class PostSave1Actor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Map Execute(ServletMessage message) throws Throwable {
        Map post=message.getContext();
        post.put("disallow_comment",post.get("disallowComment"));

        if(post.get("editorType")==null){
            post.put("editor_type", PostEditorType.MARKDOWN.getValue());
        }else{
            post.put("editor_type", post.get("editorType"));
        }
        post.put("original_content",post.get("originalContent"));
        if(post.get("editor_type").equals(PostEditorType.MARKDOWN.getValue())){
            post.put("format_content",MarkdownUtils.renderHtml((String)post.get("originalContent")));
        }else{
            post.put("format_content",post.get("originalContent"));

        }
        post.put("type",0);
        if(post.get("likes")==null||post.get("likes").toString().trim().equals("")){
            post.put("likes",0);
        }
        if(post.get("visits")==null||post.get("visits").toString().trim().equals("")){
            post.put("visits",0);
        }
        if(post.get("top_priority")==null||post.get("top_priority").toString().trim().equals("")){
            post.put("top_priority",0);
        }
        if(post.get("slug")==null||post.get("slug").toString().trim().equals("")){
            post.put("slug",post.get("title"));
        }
        post.put("siteid",Utils.getSiteId());
        post.put("status",PostStatus.valueOf((String)post.get("status")).getValue());
        if(post.get("password")==null||post.get("password").toString().trim().equals("")){

        }else{
            post.put("status",PostStatus.INTIMATE.getValue());
        }
        Object id=null;
        if(post.get("id")==null||post.get("id").toString().trim().equals("")){
            post.put("create_time",new java.sql.Timestamp(System.currentTimeMillis()));
            post.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
            post.put("edit_time",new java.sql.Timestamp(System.currentTimeMillis()));
            Map oneMap=sqlSession.selectOne("posts.qbyslug",post);
            if(oneMap!=null&&(!oneMap.isEmpty())){
                throw new AlreadyExistsException("文章别名 " + post.get("slug") + " 已存在");

            }
            sqlSession.insert("posts.i",post);
             oneMap=sqlSession.selectOne("posts.qbyslug",post);
            id=oneMap.get("id");
            //create
        }else{
            id=post.get("id");
            post.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
            post.put("edit_time",new java.sql.Timestamp(System.currentTimeMillis()));
            sqlSession.update("posts.u",post);
            //update
        }
        category(message.getContextData("categoryIds"),id);
        tags(message.getContextData("tagIds"),id);
        return post;
    }
    public void tags(List list,Object postid){
        Map map=new HashMap();
        map.put("siteid",Utils.getSiteId());
        map.put("post_id",postid);
        sqlSession.delete("posttag.dbypostid",map);
        map.put("create_time",new java.sql.Timestamp(System.currentTimeMillis()));
        map.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
        if(list==null){
            return;
        }
        for(int i=0;i<list.size();i++){
            map.put("tag_id",list.get(i));
            sqlSession.insert("posttag.i",map);

        }
    }
    public void category(List list,Object postid){
        Map map=new HashMap();
        map.put("siteid",Utils.getSiteId());
        map.put("post_id",postid);
        sqlSession.delete("post_categories.dbypostid",map);
        map.put("create_time",new java.sql.Timestamp(System.currentTimeMillis()));
        map.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
        if(list==null){
            return;
        }
        for(int i=0;i<list.size();i++){
            map.put("category_id",list.get(i));
            sqlSession.insert("post_categories.i",map);

        }

    }

}
