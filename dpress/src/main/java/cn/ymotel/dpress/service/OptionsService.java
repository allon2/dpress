package cn.ymotel.dpress.service;

import com.alicp.jetcache.anno.Cached;
import com.alicp.jetcache.anno.CreateCache;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class OptionsService {
    public static String KEY_WORDS="seo_keywords";
    public static String DESCRIPTION="seo_description";
    public static String BLOG_TITLE="blog_title";
    public static String BLOG_LOCALE="blog_locale";
    public static String  GLOBAL_ABSOLUTE_PATH_ENABLED="global_absolute_path_enabled";
    public static String BLOG_LOGO="blog_logo";
    public static String LINKS_PREFIX="links_prefix";
    public static String PHOTOS_PREFIX="photos_prefix";
    public static String SHEET_PREFIX="sheet_prefix";
    public static String JOURNALS_PREFIX="journals_prefix";

    public static String ARCHIVES_PREFIX="archives_prefix";
    public static String CATEGORIES_PREFIX="categories_prefix";
    public static String TAGS_PREFIX="tags_prefix";
    public static String SEO_SPIDER_DISABLED="seo_spider_disabled";
    public static String PATH_SUFFIX="path_suffix";

    @Autowired
    private SqlSession sqlSession;


    @Cached
    public Map getOptions(Object id){
        Map rtnMap=new HashMap();
        Map map=new HashMap();
        map.put("siteid",id);
       List ls= sqlSession.selectList("options.qall",map);
       for(int i=0;i<ls.size();i++){
           Map tMap=(Map)ls.get(i);
           String value=(String)tMap.get("option_value");
            if(value!=null){

            }
           rtnMap.put(tMap.get("option_key"),value);
       }
       return rtnMap;
    }
    @Cached
    public  <T> T getOption(Object id,Object key,T defaultValue){
        return (T)getOptions(id).getOrDefault(key,defaultValue);
    }

    @Cached
    public Boolean getBooleanOption(Object id,Object key,Boolean defaultValue){
        String ss=getOptions(id).getOrDefault(key,defaultValue).toString();
        return Boolean.valueOf(ss);
    }
    @Cached
    public String getArchives(Object id){
//        return getOption(id,ARCHIVES_PREFIX,"archivesabcedfafd");

        return getOption(id,ARCHIVES_PREFIX,"archives");
    }
    @Cached
    public String getCategories(Object id){
        return getOption(id,CATEGORIES_PREFIX,"categories");
    }
    @Cached
    public String getTags(Object id){
        return getOption(id,TAGS_PREFIX,"tags");
    }
    @Cached
    public  String getSheet(Object id){
        return  getOption(id,SHEET_PREFIX,"s");
    }

    @Cached
    public   String getPathSuffix(Object id){
        return  getOption(id,PATH_SUFFIX,"");
    }
    @Cached
    public String getPhotosPrefix(Object id){
        return  getOption(id,PHOTOS_PREFIX,"photos");
    }
    @Cached
    public String getJournalsPrefix(Object id){
        return getOption(id,JOURNALS_PREFIX,"journals");
    }
}
