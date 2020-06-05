package cn.ymotel.dpress.admin.theme;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;
import run.halo.app.exception.AlreadyExistsException;
import run.halo.app.exception.UnsupportedMediaTypeException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@ActorCfg(urlPatterns = "/api/admin/themes")
public class ThemesActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Object Execute(ServletMessage message) throws Throwable {
        Map map=new HashMap();
        map.put("key","installthemes");
        map.put("siteid",Utils.getSiteIdFromMessage(message));

        java.util.Map installthemesMap=sqlSession.selectOne("options.qoption",map);
        String themes=(String)installthemesMap.get("option_value");
        map.put("key","theme");
        java.util.Map themeMap=sqlSession.selectOne("options.qoption",map);
        String activeTheme=(String)themeMap.get("option_value");
        String[] athemes=themes.split(",");
        List rtnList=java.util.Collections.synchronizedList(new ArrayList<>());
        List<CompletableFuture> futureList=new ArrayList();
        for(int i=0;i<athemes.length;i++){
            final  String theme1=athemes[i];
            CompletableFuture future=  java.util.concurrent.CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    Map tMap=new HashMap();
                    tMap.putAll(map);
                    tMap.put("path","theme.yaml");
                    tMap.put("theme",theme1);
                    Map  tmpthemeMap= sqlSession.selectOne("dpress.qtemplate",tMap);
                    if(tmpthemeMap==null){
                        return;
                    }
                    String content=(String) tmpthemeMap.get("content");
                    Yaml yaml = new Yaml();
                    Map yamlmap =  yaml.load(content);
                    if(yamlmap.get("id").equals(activeTheme)){
                        yamlmap.put("activated",true);
                    }else{
                        yamlmap.put("activated",false);
                    }
                    yamlmap.put("screenshots","/themes/"+yamlmap.get("id")+"/screenshot");
                    rtnList.add(yamlmap);
                }
            })
           ;
            futureList.add(future);
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0] )).join();
        return rtnList;

    }


}
