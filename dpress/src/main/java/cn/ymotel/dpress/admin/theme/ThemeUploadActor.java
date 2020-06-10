package cn.ymotel.dpress.admin.theme;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.response.ResponseViewType;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.ThemeZipUtils;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.admin.install.InstallSiteActor;
import cn.ymotel.dpress.service.SiteThemeService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.yaml.snakeyaml.Yaml;
import run.halo.app.exception.AlreadyExistsException;
import run.halo.app.exception.UnsupportedMediaTypeException;
import run.halo.app.handler.theme.config.support.ThemeProperty;
import run.halo.app.utils.FilenameUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@ActorCfg(urlPatterns = "/api/admin/themes/upload")
public class ThemeUploadActor  implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;

    @Autowired
    InstallSiteActor installSiteActor;


    @Override
    public Object Execute(ServletMessage message) throws Throwable {
        MultipartFile multipartFile = message.getFile("file");
        if (!StringUtils.endsWithIgnoreCase(multipartFile.getOriginalFilename(), ".zip")) {
            throw new UnsupportedMediaTypeException("不支持的文件类型: " + multipartFile.getContentType()).setErrorData(multipartFile.getOriginalFilename());
        }
        Map dataMap=ThemeZipUtils.installTheme(sqlSession,new ByteArrayInputStream(message.getFileBytes("file")),Utils.getSiteIdFromMessage(message));
//        Map zipMap = ThemeZipUtils.getDataFromBytes(message.getFileBytes("file"));
//        zipMap = ThemeZipUtils.rmBaseMap(zipMap);
//
//
//        byte[] yamlbytes = (byte[]) zipMap.get("theme.yaml");
//        Yaml yaml = new Yaml();
//        Map map =  yaml.load(new ByteArrayInputStream(yamlbytes));
//
//        String themeName = (String) map.get("id");
//        {
//            Map paramMap=new HashMap();
//        paramMap.put("siteid", Utils.getSiteIdFromMessage(message));
//            paramMap.put("key","installthemes");
//       Map data= sqlSession.selectOne("options.qoption",paramMap);
//       if(data==null||data.isEmpty()){
//           //
//           paramMap.put("create_time",new java.sql.Timestamp(System.currentTimeMillis()));
//           paramMap.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
//           paramMap.put("option_key","installthemes");
//           paramMap.put("option_value",themeName);
//           paramMap.put("type","0");
//
//           sqlSession.insert("options.ioption",paramMap);
//       }else{
//           String value=(String)data.get("option_value");
//           if(value.indexOf(themeName)>=0){
//               //抛错错误
//               throw new AlreadyExistsException("当前安装的主题已存在");
//           }
//           value=value+","+themeName;
//           data.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
//           data.put("option_value",value);
//           data.put("siteid",Utils.getSiteIdFromMessage(message));
//           data.put("option_key","installthemes");
//           sqlSession.update("options.uoption",data);
//       }
//    }
//
//
//        for(java.util.Iterator iter=zipMap.entrySet().iterator();iter.hasNext();){
//            Map.Entry entry=(Map.Entry)iter.next();
//            String path=(String)entry.getKey();
//            byte[] bytes=(byte[])entry.getValue();
//            Map paramMap=new HashMap();
//            if(SiteThemeService.istext(path)){
//                paramMap.put("content",new String(bytes,"UTF-8"));
//            }else{
//                paramMap.put("bcontent",bytes);
//            }
//            paramMap.put("lastModified",new java.sql.Timestamp(System.currentTimeMillis()));
//            paramMap.put("path",path);
//            paramMap.put("theme",themeName);
//            paramMap.put("siteid", Utils.getSiteIdFromMessage(message));
//
//            sqlSession.insert("dpress.itemplate",paramMap);
//
//        }

        Map rtnMap=new HashMap();
        rtnMap.put("data",dataMap);
         return rtnMap;
    }


}
