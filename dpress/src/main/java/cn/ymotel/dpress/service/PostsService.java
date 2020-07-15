package cn.ymotel.dpress.service;

import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.entity.mapper.PostsMapper;
import cn.ymotel.dpress.entity.model.*;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import run.halo.app.model.properties.PostProperties;
import run.halo.app.utils.HaloUtils;
import run.halo.app.utils.MarkdownUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PostsService {
    @Autowired
    private SqlSession sqlSession;

    @Autowired
    OptionsService optionsService;
    @Cached(expire =60*30 )
    public Long count(Object siteid,Object status){
        Map data=new HashMap<>();
        data.put("siteid",siteid);
        data.put("status",status);
        return sqlSession.selectOne("posts.qcountbynormal",data);
    }
    @Cached
    public Long count(Object siteid,Object status,String keyword){
        Map data=new HashMap<>();
        data.put("siteid",siteid);
        data.put("status",status);
        data.put("keyword","%"+keyword+"%");

        return sqlSession.selectOne("posts.qsearchcountbynormal",data);
    }
    public long getMaxId(Object siteid){
        Map data=new HashMap();
        data.put("siteid",siteid);
        data.put("status",0);
        return  sqlSession.selectOne("posts.qmaxid",data);
    }
    public long getMinId(Object siteid){
        Map data=new HashMap();
        data.put("siteid",siteid);
        data.put("status",0);
        return  sqlSession.selectOne("posts.qminid",data);
    }
    public long getOffSet(long min,long max){
        if(max<=min){
            return min;
        }
        return ThreadLocalRandom.current().nextLong(min,max);
    }
    @Cached
    public List<Posts> getRandomPosts(Object siteid,int size){
        long max=getMaxId(siteid);
        long min=getMinId(siteid);
        List rtnList=new ArrayList();
        List<CompletableFuture>  futureList= Collections.synchronizedList(new ArrayList<>());

        for(int i=0;i<size;i++) {
            CompletableFuture future1=  java.util.concurrent.CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    Posts posts=sqlSession.getMapper(PostsMapper.class).selectById(getOffSet(min,max));
                    if(!siteid.toString().equals(posts.getSiteid()+"")){
                        return;
                    }
                    if(posts.getStatus()!=0){
                        return;
                    }
                    if(posts.getType()!=0){
                        return;
                    }
                    posts.setFullPath(buildFullPath(siteid,posts.getSlug()));
                    rtnList.add(posts);
                }
            });
            futureList.add(future1);

        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();

        return rtnList;
    }
    public String buildFullPath(Object siteid,String slug){
        return "/"+optionsService.getArchives(siteid)+"/"+slug+optionsService.getPathSuffix(siteid);
     }
     @Cached
     public List listLatest(Object siteid,int step){
         Map map=new HashMap();
         map.put("siteid",siteid);
         map.put("status",0);
         map.put("type",0);
         map.put("step",step);
         List list= sqlSession.selectList("posts.qorderbycreate_time",map);
         for(int i=0;i<list.size();i++) {
             Map tData = (Map) list.get(i);
             tData.put("fullPath", buildFullPath(siteid, (String) tData.get("slug")));
         }
         return  list;
     }
     @Autowired
     TagTagsService tagTagsService;
    public List<Posts> getPostsByTagSlug(Object siteid,String slug,int status) {
        Tags tags=tagTagsService.getTagBySlug(siteid,slug);
        if(tags==null){
            return  null;
        }
        return  getPostsByPostTagId(siteid,tags.getId(),status);
    }
        @Resource
    public PostsMapper postsMapper;

    @Autowired
    PostTagsService postTagsService;
    public List<Posts> getPostsByPostTagId(Object siteid,int tagid,int status){
        List<PostTags>  tagsList= postTagsService.getPostTagsbyTagId(siteid,tagid);
        List list=new ArrayList();
        tagsList.forEach(postTags -> {
            list.add(postTags.getPostId());
        });

        return getPostsByIds(list,status,siteid);

    }


    @Autowired
    private  CategorysService categorysService;
    public List<Posts> getPostsByCategorieSlug(Object siteid,String slug,int status){
        Categories categories= categorysService.getCategorybySlug(siteid,slug);
        if(categories==null){
            return null;
        }
        return getPostsByPostCategoriesId(siteid,categories.getId(),status);
    }
    @Autowired
    PostCategorysService postCategorysService;
    public List<Posts> getPostsByPostCategoriesId(Object siteid, Object categoryId,int status){
        List<PostCategories> categories= postCategorysService.findPostCategoriesBycategoryId(siteid,categoryId);
        return getPostsByPostCategoriess(categories,status,siteid);
    }
    public List<Posts> getPostsByPostCategoriess(List<PostCategories> categories,int status,Object siteid){
        List list=new ArrayList();
        categories.forEach(postCategories -> list.add(postCategories.getPostId()));
        return getPostsByIds(list,status,siteid);
//        List<Posts> postsList=postsMapper.selectBatchIds(list);
//        List<Posts> rtnList=new ArrayList();
//        postsList.forEach(posts -> {
//            if(posts.getStatus().intValue()==status){
//                posts.setFullPath(buildFullPath(siteid,posts.getSlug()));
//                setFormatContent(posts);
//                posts.setSummary(generateSummary(posts.getFormatContent()));
//                rtnList.add(posts);
//            }
//        });
//
//        return rtnList;
    }
    public List<Posts> getPostsByIds(List<Integer> ids,int status,Object siteid){
        List<Posts> postsList=postsMapper.selectBatchIds(ids);

        List<Posts> rtnList=new ArrayList();

        postsList.forEach(posts -> {
            if(posts.getStatus().intValue()==status){
                posts.setFullPath(buildFullPath(siteid,posts.getSlug()));
                setFormatContent(posts);
                posts.setSummary(generateSummary(posts.getFormatContent()));
                rtnList.add(posts);
            }
        });
        return  rtnList;
    }
    public Posts getPostsById(Integer id,Object siteid){
        Map map=new HashMap();
        map.put("siteid",siteid);
        map.put("id",id);
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.allEq(map);
        return postsMapper.selectOne(queryWrapper);
    }
@Cached
     public List listYearArchives(Object siteid){
         Map map=new HashMap();
         map.put("siteid",siteid);
         map.put("status",0);
         map.put("type",0);
         List list= sqlSession.selectList("posts.qorderbycreate_time",map);
         Map dataMap=new HashMap();
         for(int i=0;i<list.size();i++){
             Map tData=(Map)list.get(i);
             tData.put("fullPath",buildFullPath(siteid,(String)tData.get("slug")));
             java.util.Date createTime=(java.util.Date)tData.get("createTime");
             java.util.Calendar calendar=java.util.Calendar.getInstance();
             calendar.setTime(createTime);
             int year=calendar.get(Calendar.YEAR);
            List posts=(List) dataMap.get(year);
            if(posts==null){
                posts=new ArrayList();
                dataMap.put(year,posts);
            }
             posts.add(tData);
         }
         List rtnList=new ArrayList();
         for(java.util.Iterator iter=dataMap.entrySet().iterator();iter.hasNext();){
             Map.Entry entry=(Map.Entry)iter.next();
             Map tMap=new HashMap();
             tMap.put("year",entry.getKey());
             tMap.put("posts",entry.getValue());
             rtnList.add(tMap);
         }
         return rtnList;
     }
     @Cached
    public List listMonthArchives(Object siteid){
        Map map=new HashMap();
        map.put("siteid",siteid);
        map.put("status",0);
        map.put("type",0);
       List list= sqlSession.selectList("posts.qorderbycreate_time",map);
        MultiKeyMap<Integer,List> yearMonthMap = new MultiKeyMap();
       for(int i=0;i<list.size();i++){
        Map tData=(Map)list.get(i);
        tData.put("fullPath",buildFullPath(siteid,(String)tData.get("slug")));
        java.util.Date createTime=(java.util.Date)tData.get("createTime");
        java.util.Calendar calendar=java.util.Calendar.getInstance();
           calendar.setTime(createTime);
           int year=calendar.get(Calendar.YEAR);
           int month=calendar.get(Calendar.MONTH)+1;
           List posts=yearMonthMap.get(year,month);
           if(posts==null){
               posts=new ArrayList();
               yearMonthMap.put(year,month,posts);
           }
           posts.add(tData);
       }
       List rtnList=new ArrayList();
        for(Map.Entry<MultiKey<? extends Integer>, List>  entry: yearMonthMap.entrySet()){
            Map dataMap=new HashMap();
            dataMap.put("year",entry.getKey().getKey(0));
            dataMap.put("month",entry.getKey().getKey(1));
            dataMap.put("posts",entry.getValue());

         rtnList.add(dataMap);
        }
        return rtnList;
    }
    private final Pattern summaryPattern = Pattern.compile("\\s*|\t|\r|\n");
    public static void setFormatContent(Posts posts){
        if(posts.getFormatContent()!=null&&!posts.getFormatContent().trim().equals("")){
            return;
        }
        String formContent= null;
        if(posts.getEditorType()==0) {
            formContent= MarkdownUtils.renderHtml(posts.getOriginalContent());
        }else {
            formContent=posts.getOriginalContent();
        }
        posts.setFormatContent(formContent);
    }
    @NonNull
    protected String generateSummary(@NonNull String htmlContent) {
        Assert.notNull(htmlContent, "html content must not be null");

        String text = HaloUtils.cleanHtmlTag(htmlContent);

        Matcher matcher = summaryPattern.matcher(text);
        text = matcher.replaceAll("");

        // Get summary length
        Integer summaryLength = 150;

        return StringUtils.substring(text, 0, summaryLength);
    }
}
