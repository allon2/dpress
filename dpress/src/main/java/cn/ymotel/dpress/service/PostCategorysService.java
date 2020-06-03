package cn.ymotel.dpress.service;

import cn.ymotel.dpress.entity.mapper.PostCategoriesMapper;
import cn.ymotel.dpress.entity.model.PostCategories;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PostCategorysService {
//    @Autowired
//    private SqlSession sqlSession;
    @Resource
   private PostCategoriesMapper postCategoriesMapper;

    public List<PostCategories> findPostCategoriesBycategoryId(Object siteid, Object categoryId){
        Map map=new HashMap<>();
        map.put("siteid",siteid);
        map.put("category_id",categoryId);
       List<PostCategories> list= postCategoriesMapper.selectByMap(map);
        return  list;
    }
    public List<PostCategories> listPostCategoriesbyId(Object siteid,Object postid){
        Map map=new HashMap<>();
        map.put("siteid",siteid);
        map.put("post_id",postid);
        List<PostCategories> list= postCategoriesMapper.selectByMap(map);
        return  list;
    }


}
