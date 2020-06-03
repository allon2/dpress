package cn.ymotel.dpress.service;

import cn.ymotel.dpress.entity.mapper.CommentsMapper;
import cn.ymotel.dpress.entity.model.Comments;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static run.halo.app.model.support.HaloConst.URL_SEPARATOR;

@Component
public class CommentsService {
    @Autowired
   private OptionsService optionsService;
    @Resource
    private  CommentsMapper commentsMapper;
    public long count(Object siteid){
        Map map=new HashMap<>();
        map.put("siteid",siteid);
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.allEq(map);
       return commentsMapper.selectCount(queryWrapper);
    }
    @Autowired
    private PostsService postsService;
    public PageImpl Latest(Object siteid,int topN,int status){
        Map map=new HashMap<>();
        map.put("siteid",siteid);
        map.put("status",status);
        QueryWrapper<Comments> queryWrapper=new QueryWrapper();
        queryWrapper.allEq(map);
        queryWrapper.orderByDesc("create_time");
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Comments> page=new com.baomidou.mybatisplus.extension.plugins.pagination.Page(0,topN);

//        fullPath.append(archivesPrefix)
//                .append(URL_SEPARATOR)
//                .append(post.getSlug())
//                .append(pathSuffix);

        IPage ipage=  commentsMapper.selectPage(page,queryWrapper);
        ipage.getRecords().forEach(new Consumer() {
            @Override
            public void accept(Object o) {
                Comments comments=(Comments)o;
               String slug= postsService.getPostsById(comments.getPostId(),siteid).getSlug();
                String fullPath="/"+optionsService.getArchives(siteid)+"/"+slug+optionsService.getPathSuffix(siteid);
                comments.setFullPath(fullPath);
            }
        });
        return     new PageImpl(ipage.getRecords(), PageRequest.of(0,topN),ipage.getTotal());

    }
}
