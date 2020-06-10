package cn.ymotel.dpress.admin.theme;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.entity.mapper.DpressTemplateMapper;
import cn.ymotel.dpress.entity.model.DpressTemplate;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.yaml.snakeyaml.Yaml;

import java.util.*;

@ActorCfg(urlPatterns = "/api/admin/themes/userthemes")
public class UserThemesActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Object Execute(ServletMessage message) throws Throwable {
        List rtnList= Collections.synchronizedList(new ArrayList<>());

        Map params=new HashMap();

        String activeTheme=getActivateTheme(Utils.getSiteIdFromMessage(message));


        Map dpressMap=new HashMap();
        dpressMap.put("siteid",Utils.getSiteIdFromMessage(message));
        dpressMap.put("path","theme.yaml");

        List <DpressTemplate> dpressTemplates=sqlSession.getMapper(DpressTemplateMapper.class).selectByMap(dpressMap);
        for(int i=0;i<dpressTemplates.size();i++){
            DpressTemplate dpressTemplate=dpressTemplates.get(i);

            Yaml yaml = new Yaml();
            Map yamlmap =  yaml.load(dpressTemplate.getContent());
            yamlmap.put("screenshots","/themes/usertheme/"+yamlmap.get("id")+"/screenshot");
            rtnList.add(yamlmap);
            if(yamlmap.get("id").equals(activeTheme)){
                yamlmap.put("activated",true);
            }else{
                yamlmap.put("activated",false);
            }
            yamlmap.put("usertheme",true);
            yamlmap.put("systemtheme",false);

        }
        return rtnList;
//                Map map=new HashMap();
//        map.put("key","installthemes");
//        map.put("siteid",Utils.getSiteIdFromMessage(message));
//
//        java.util.Map installthemesMap=sqlSession.selectOne("options.qoption",map);
//        String themes=(String)installthemesMap.get("option_value");
////        return rtnList;
//
//        map.put("key","theme");
//        java.util.Map themeMap=sqlSession.selectOne("options.qoption",map);
//        String activeTheme=(String)themeMap.get("option_value");
//        String[] athemes=themes.split(",");
//        List rtnList=java.util.Collections.synchronizedList(new ArrayList<>());
//        List<CompletableFuture> futureList=new ArrayList();
//        for(int i=0;i<athemes.length;i++){
//            final  String theme1=athemes[i];
//            CompletableFuture future=  java.util.concurrent.CompletableFuture.runAsync(new Runnable() {
//                @Override
//                public void run() {
//                    Map tMap=new HashMap();
//                    tMap.putAll(map);
//                    tMap.put("path","theme.yaml");
//                    tMap.put("theme",theme1);
//                    Map  tmpthemeMap= sqlSession.selectOne("dpress.qtemplate",tMap);
//                    if(tmpthemeMap==null){
//                        return;
//                    }
//                    String content=(String) tmpthemeMap.get("content");
//                    Yaml yaml = new Yaml();
//                    Map yamlmap =  yaml.load(content);
//                    if(yamlmap.get("id").equals(activeTheme)){
//                        yamlmap.put("activated",true);
//                    }else{
//                        yamlmap.put("activated",false);
//                    }
//                    yamlmap.put("screenshots","/themes/"+yamlmap.get("id")+"/screenshot");
//                    rtnList.add(yamlmap);
//                }
//            })
//           ;
//            futureList.add(future);
//        }
//        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0] )).join();
//        return rtnList;

    }
    public String getActivateTheme(Object siteid){
                Map map=new HashMap();
        map.put("key","theme");
        map.put("siteid",siteid);
        Map rtnMap=sqlSession.selectOne("options.qoption",map);
        String theme=(String)rtnMap.get("option_value");
        return  theme;
//            Map tMap=new HashMap();
//                            tMap.put("path","theme.yaml");
//                    tMap.put("theme",theme);
//                    tMap.put("siteid",siteid);
//                    Map  tmpthemeMap= sqlSession.selectOne("dpress.qtemplate",tMap);
//        String content=(String) tmpthemeMap.get("content");
//        Yaml yaml = new Yaml();
//        Map yamlmap =  yaml.load(content);
    }


}
