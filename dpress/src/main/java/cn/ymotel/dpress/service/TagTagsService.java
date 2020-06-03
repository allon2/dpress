package cn.ymotel.dpress.service;

import cn.ymotel.dpress.entity.mapper.TagsMapper;
import cn.ymotel.dpress.entity.model.Tags;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TagTagsService {
    @Autowired
    private SqlSession sqlSession;
    @Autowired
    OptionsService optionsService;
    @Resource
    TagsMapper tagsMapper;
    public Tags getTagBySlug(Object siteid,String slug){
        QueryWrapper queryWrapper=new QueryWrapper();
        Map map=new HashMap();
        map.put("siteid",siteid);
        map.put("slug",slug);
        queryWrapper.allEq(map);
        return tagsMapper.selectOne(queryWrapper);
    }
    public List getTagsByPostId(Object siteid, Object postid){
        Map map=new HashMap();
        map.put("postid",postid);
        map.put("siteid",siteid);
      List list=  sqlSession.selectList("tags.qtagsbypostid",map);
      for(int i=0;i<list.size();i++){
          Map data=(Map)list.get(i);
          String path=optionsService.getTags(siteid)+"/"+data.get("slug")+optionsService.getPathSuffix(siteid);
          data.put("fullPath",path);

      }
      return list;
    }
    @Cached
    public Long count(Object siteid){
        Map data=new HashMap<>();
        data.put("siteid",siteid);
        return sqlSession.selectOne("tags.qcount",data);
    }
}
