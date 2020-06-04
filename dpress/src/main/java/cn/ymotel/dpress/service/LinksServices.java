package cn.ymotel.dpress.service;

import cn.ymotel.dpress.entity.mapper.LinksMapper;
import cn.ymotel.dpress.entity.model.Links;
import cn.ymotel.dpress.entity.model.Photos;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LinksServices {
    @Autowired
    private SqlSession sqlSession;

    @Cached
    public long count(Object siteid){
        Map map=new HashMap<>();
        map.put("siteid",siteid);
       return  sqlSession.selectOne("links.qcount",map);
    }
    @Cached
    public List listTeams(Object siteid, String sort){
        Map map=new HashMap<>();
        map.put("siteid",siteid);
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.allEq(map);
        queryWrapper.orderByDesc(sort);
        List<Photos> list= linksMapper.selectList(queryWrapper);
        Map<String,List<Photos>> teamMap=new HashMap();
        list.forEach(photos -> {
            List ls=(List)teamMap.get(photos.getTeam());
            if(ls==null){
                ls=new ArrayList();
                teamMap.put(photos.getTeam(),ls);
            }
            ls.add(photos);

        });
        List rtnList=new ArrayList();
        teamMap.forEach((key, photos) -> {
            Map tMap=new HashMap();
            tMap.put("team",key);
            tMap.put("links",photos);
        });
        return rtnList;
    }
    @Resource
    private LinksMapper linksMapper;
    @Cached
    public List<Links> listAll(Object siteid){
        Map map=new HashMap<>();
        map.put("siteid",siteid);
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.allEq(map);
        return   linksMapper.selectList(queryWrapper);
    }
}
