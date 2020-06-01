package cn.ymotel.dpress.service;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class LinksServices {
    @Autowired
    private SqlSession sqlSession;

    public long count(Object siteid){
        Map map=new HashMap<>();
        map.put("siteid",siteid);
       return  sqlSession.selectOne("links.qcount",map);
    }
}
