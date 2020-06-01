package cn.ymotel.dpress.service;

import com.alicp.jetcache.anno.Cached;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TagTagsService {
    @Autowired
    private SqlSession sqlSession;
    @Autowired
    OptionsService optionsService;

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
