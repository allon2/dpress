package cn.ymotel.dpress.service;

import com.alicp.jetcache.anno.Cached;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PostsService {
    @Autowired
    private SqlSession sqlSession;

    @Cached
    public Long count(Object siteid,Object status){
        Map data=new HashMap<>();
        data.put("siteid",siteid);
        data.put("status",status);
        return sqlSession.selectOne("posts.qcountbynormal",data);
    }
}
