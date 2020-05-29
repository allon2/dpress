package cn.ymotel.dpress.service;

import cn.hutool.core.collection.CollUtil;
import com.alicp.jetcache.anno.Cached;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CategorysService {
    @Autowired
    private SqlSession sqlSession;

    @Cached
    public Long count(Object siteid){
        Map data=new HashMap<>();
        data.put("siteid",siteid);
        return sqlSession.selectOne("categories.qcount",data);
    }
}
