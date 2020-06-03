package cn.ymotel.dpress.service;

import cn.ymotel.dpress.entity.mapper.PhotosMapper;
import cn.ymotel.dpress.entity.model.Photos;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
@Component
public class PhotosService {
    @Resource
    PhotosMapper photosMapper;
    public List<Photos> listbyTeam(Object siteid,String team,String sort){
        Map map=new HashMap<>();
        map.put("siteid",siteid);
        map.put("team",team);
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.allEq(map);
        queryWrapper.orderByDesc(sort);
        List<Photos> list= photosMapper.selectList(queryWrapper);
        return  list;
    }
    public List listbyTeams(Object siteid,String sort){
        Map map=new HashMap<>();
        map.put("siteid",siteid);
         QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.allEq(map);
        queryWrapper.orderByDesc(sort);
        List<Photos> list= photosMapper.selectList(queryWrapper);
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
            tMap.put("photos",photos);
        });
        return rtnList;
    }
    public List<Photos> listAll(Object siteid){
        Map map=new HashMap<>();
        map.put("siteid",siteid);
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.allEq(map);
       return photosMapper.selectList(queryWrapper);
    }
    public long count(Object siteid){
        Map map=new HashMap<>();
        map.put("siteid",siteid);
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.allEq(map);
      return   photosMapper.selectCount(queryWrapper);
    }
}
