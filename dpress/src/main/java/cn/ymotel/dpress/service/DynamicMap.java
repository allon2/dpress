package cn.ymotel.dpress.service;

import java.util.HashMap;
import java.util.Map;

public class DynamicMap  extends HashMap {

    @Override
    public Object get(Object key) {

        System.out.println("DaynamicMap----"+key);
        Object obj= super.get(key);
//        if(obj==null){
//            if(key.equals("seo_spider_disabled")){
//                return false;
//            }
//        }
        return obj;
    }
}
