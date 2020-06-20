package run.halo.app.core.freemarker.tag;


import cn.ymotel.dpress.service.PostsService;
import freemarker.core.Environment;
import freemarker.template.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class RandomRecommendTagDirective implements TemplateDirectiveModel {
    @Autowired
    private SqlSession sqlSession;

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        Object siteid=null;
        try {
            siteid= ((SimpleNumber)env.getVariable("_siteid")).getAsNumber();
        } catch (java.lang.Throwable e) {
            e.printStackTrace();
        }
        String baseurl=null;
        try{
            baseurl=((SimpleScalar)env.getVariable("_baseurl")).getAsString();
        }catch (Exception e){}
        final DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);


       String size= params.get("size").toString();

            env.setVariable("posts", builder.build().wrap(postsService.getRandomPosts(siteid,Integer.parseInt(size))));


        body.render(env.getOut());

    }
    @Autowired
    PostsService postsService;
//    @Cached
//    List getPosts(Object siteid,String size){
//        long max=getMaxId(siteid);
//        long min=getMinId(siteid);
//
//        sqlSession.getMapper(PostsMapper.class).selectByMap();
//        Map map=new HashMap();
//        map.put("status",0);
//        map.put("type",0);
//        map.put("siteid",siteid);
//        map.put("startid",getOffSet(min,max));
//        map.put("size",Integer.parseInt(size));
//        List rtnList= sqlSession.selectList("posts.qpostslimit2",map);
//        for(int i=0;i<rtnList.size();i++) {
//            Map tData = (Map) rtnList.get(i);
//            tData.put("fullPath", buildFullPath(siteid, (String) tData.get("slug")));
//        }
//        return rtnList;
//    }
//    @Cached
//    List getPosts(Object siteid,String size){
//        long max=getMaxId(siteid);
//        long min=getMinId(siteid);
//        int isize=Integer.parseInt(size);
//        List rtnList=new ArrayList();
//        List<CompletableFuture>  futureList= Collections.synchronizedList(new ArrayList<>());
//
//        for(int i=0;i<isize;i++) {
//            CompletableFuture future1=  java.util.concurrent.CompletableFuture.runAsync(new Runnable() {
//                @Override
//                public void run() {
//                    Posts posts=sqlSession.getMapper(PostsMapper.class).selectById(getOffSet(min,max));
//                    if(!siteid.toString().equals(posts.getSiteid()+"")){
//                        return;
//                    }
//                    if(posts.getStatus()!=0){
//                        return;
//                    }
//                    if(posts.getType()!=0){
//                        return;
//                    }
//                    posts.setFullPath(buildFullPath(siteid,posts.getSlug()));
//                    rtnList.add(posts);
//                }
//            });
//            futureList.add(future1);
//
//        }
//        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
//
////            Map map=new HashMap();
////            map.put("status",0);
////            map.put("type",0);
////            map.put("siteid",siteid);
////            map.put("startid",getOffSet(min,max));
////            map.put("size",Integer.parseInt(size));
////            List rtnList= sqlSession.selectList("posts.qpostslimit2",map);
////            for(int i=0;i<rtnList.size();i++) {
////                Map tData = (Map) rtnList.get(i);
////                tData.put("fullPath", buildFullPath(siteid, (String) tData.get("slug")));
////            }
//            return rtnList;
//    }
//    @Autowired
//    OptionsService optionsService;
//    public String buildFullPath(Object siteid,String slug){
//        return "/"+optionsService.getArchives(siteid)+"/"+slug+optionsService.getPathSuffix(siteid);
//    }
//    public long getOffSet(long min,long max){
//        if(max<=min){
//            return min;
//        }
//        return ThreadLocalRandom.current().nextLong(min,max);
////        long rangeLong = min + (((long) (new Random().nextDouble() * (max - min))));
////            return rangeLong;
//    }
//    public long getMaxId(Object siteid){
//        Map data=new HashMap();
//        data.put("siteid",siteid);
//        data.put("status",0);
//        return  sqlSession.selectOne("posts.qmaxid",data);
//    }
//    public long getMinId(Object siteid){
//        Map data=new HashMap();
//        data.put("siteid",siteid);
//        data.put("status",0);
//        return  sqlSession.selectOne("posts.qminid",data);
//    }
}
