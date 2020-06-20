package cn.ymotel.dpress.schedule;

import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.entity.mapper.DpressSiteinfoMapper;
import cn.ymotel.dpress.entity.mapper.DpressTemplateMapper;
import cn.ymotel.dpress.entity.model.DpressSiteinfo;
import cn.ymotel.dpress.service.OptionsService;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
@Component
@EnableScheduling
public class BaiduIndex {

    @Autowired
    SqlSession sqlSession;
    @Autowired
    OptionsService optionsService;

    /**fixedRate:上一次开始执行时间点之后10分再执行*/
    @Scheduled(fixedRate = 1000*60*10)
    @Async
    public void push2Baidu(){
        if(!Utils.isInstall()){
            return;
        }
        List<DpressSiteinfo> siteinfos=sqlSession.getMapper(DpressSiteinfoMapper.class).selectByMap(new HashMap<>());
        for(int i=0;i<siteinfos.size();i++) {
            Boolean  optioncount=optionsService.getBooleanOption(siteinfos.get(i).getId(),"baiduziyuan_disabled",Boolean.FALSE);
            if(!optioncount){
                continue;
            }
            updateSite(siteinfos.get(i));
        }
    }
    public void updateSite(DpressSiteinfo dpressSiteinfo){
        List urls = new ArrayList();
        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());


        Map map = new HashMap<>();
        map.put("status", 0);
        map.put("siteid", dpressSiteinfo.getId());
        map.put("size", 1000);
        map.put("date", date);
        Long count = sqlSession.selectOne("posts.qbaiducount", map);
        int ioptioncount=optionsService.getOption(dpressSiteinfo.getId(),"baiduziyuan_daysize",100000);
//        int ioptioncount=Integer.parseInt(optioncount);
        if (count >=ioptioncount) {
            return;
        }

        String baseurl=optionsService.getOption(dpressSiteinfo.getId(),"blog_url","");

        List baiduList = sqlSession.selectList("posts.qbaidunull", map);
        for (int i = 0; i < baiduList.size(); i++) {
            Map rtnMap = (Map) baiduList.get(i);
            urls.add(baseurl+"/"+optionsService.getArchives(dpressSiteinfo.getId())+"/" + rtnMap.get("slug"));

        }


        try {
            boolean b = post2Bae(optionsService.getOption(dpressSiteinfo.getId(),"baiduziyuan_api",""), (String[]) urls.toArray(new String[0]));
           if(b){
            for (int i = 0; i < baiduList.size(); i++) {
                Map tMap = (Map) baiduList.get(i);
                tMap.put("date", date);
                sqlSession.update("posts.ubaidudate", tMap);
            }
           }
        } catch (java.lang.Throwable e) {
            e.printStackTrace();
        }

    }


    /**
     * 需要通过post方法 推送数据到服务器中
     * urlPath 百度的推送路径 http://data.zz.baidu.com/urls?site=hkjhjgc.duapp.com&token=vLebVefzx38Zin5D
     * urls 推送的路径地址
     * @return
     */
    public static boolean post2Bae(String urlPath,String[] urls) throws java.lang.Throwable {
        String url = null;
        String param = "";
        for(String s : urls){
            param += s+"\n";
        }

            CloseableHttpClient httpClient = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(300 * 1000)
                    .setConnectTimeout(300 * 1000)
                    .build();
            url = urlPath;
            HttpPost post = new HttpPost(url);
            post.setConfig(requestConfig);
            post.setHeader("Content-Type", "text/plain");
            StringEntity postingString = new StringEntity(param,"utf-8");
            post.setEntity(postingString);
            HttpResponse response = httpClient.execute(post);
            String content = EntityUtils.toString(response.getEntity());
            System.out.println(urlPath+"--"+content);
            Map rtnMap=(Map)JSON.parse(content);
//            {"remain":1000,"success":1000}
//            {"remain":0,"success":1000}
//            {"error":400,"message":"over quota"}
            if(rtnMap.containsKey("success")){
                int success=(int)rtnMap.get("success");
                if(success!=urls.length){
                    return  false;

                }
            }else{
                return  false;

            }
            return true;


    }


}
