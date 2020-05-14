package cn.ymotel.dpress.admin.options;

import java.util.HashMap;
import java.util.Map;

public class OptionClassConvert {
    private static Map<String,Class> optionsClass=new HashMap(){
        {
            put("email_ssl_port", Integer.class);
            put("global_absolute_path_enabled", Boolean.class);
            put("email_enabled", Boolean.class);
            put("attachment_upload_max_files", Integer.class);
            put("comment_api_enabled", Boolean.class);
            put("developer_mode", Boolean.class);
            put("comment_range", Integer.class);
            put("seo_spider_disabled", Boolean.class);
            put("is_installed", Boolean.class);
            put("journals_page_size", Integer.class);
            put("comment_page_size",Integer.class);
            put("attachment_upload_image_preview_enable", Boolean.class);
            put("comment_reply_notice", Boolean.class);
            put("comment_ban_time",Integer.class);
            put("birthday",Long.class);
            put("comment_new_notice", Boolean.class);
            put("photos_page_size",Integer.class);
            put("attachment_upload_max_parallel_uploads",Integer.class);
            put("rss_page_size",Integer.class);
            put("api_enabled", Boolean.class);
            put("post_index_page_size",Integer.class);
            put("post_archives_page_size",Integer.class);
            put("comment_new_need_check", Boolean.class);
            put("post_summary_length",Integer.class);


        }
    };
    public static void StringConvert(String key,Map data){
        Class clazz=optionsClass.get(key);
        if(clazz==null){
            return ;
        }
        String value=(String) data.get(key);

        if(clazz.isAssignableFrom(Boolean.class)){
            data.put(key,Boolean.valueOf(value));
        }
        if(clazz.isAssignableFrom(Long.class)){
            data.put(key,Long.valueOf(value));
        }
        if(clazz.isAssignableFrom(Integer.class)){
            data.put(key,Integer.valueOf(value));
        }
    }

    public static void ConvertString(String key,Map data){
        Class clazz=optionsClass.get(key);
        if(clazz==null){
            return ;
        }
        Object value= data.get(key);

        if(clazz.isAssignableFrom(Boolean.class)){
            data.put(key,value.toString());
        }

    }
}
