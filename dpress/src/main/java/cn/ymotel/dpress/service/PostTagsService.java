package cn.ymotel.dpress.service;

import cn.ymotel.dpress.entity.mapper.PostTagsMapper;
import cn.ymotel.dpress.entity.model.PostTags;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Component;
import run.halo.app.config.HaloConfiguration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PostTagsService {
    @Resource
    PostTagsMapper postTagsMapper;

    public List<PostTags> getPostTagsbyTagId(Object siteid, Integer tagid){
        Map map=new HashMap<>();
        map.put("siteid",siteid);
        map.put("tag_id",tagid);

         return postTagsMapper.selectByMap(map);
    }

}
