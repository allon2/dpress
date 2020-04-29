package cn.ymotel.dpress.admin.theme;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.response.ResponseViewType;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
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
    @Override
    public Object Execute(ServletMessage message) throws Throwable {
        MultipartFile multipartFile = message.getFile("file");
        if (!StringUtils.endsWithIgnoreCase(multipartFile.getOriginalFilename(), ".zip")) {
            throw new UnsupportedMediaTypeException("不支持的文件类型: " + multipartFile.getContentType()).setErrorData(multipartFile.getOriginalFilename());
        }
        Map zipMap = getDataFromBytes(message.getFileBytes("file"));
        zipMap = rmBaseMap(zipMap);


        byte[] yamlbytes = (byte[]) zipMap.get("theme.yaml");
        Yaml yaml = new Yaml();
        Map map =  yaml.load(new ByteArrayInputStream(yamlbytes));

        String themeName = (String) map.get("id");
        {
            Map paramMap=new HashMap();
        paramMap.put("siteid", Utils.getSiteIdFromMessage(message));
            paramMap.put("key","installthemes");
       Map data= sqlSession.selectOne("options.qoption",paramMap);
       if(data==null||data.isEmpty()){
           //
           paramMap.put("create_time",new java.sql.Timestamp(System.currentTimeMillis()));
           paramMap.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
           paramMap.put("option_key","installthemes");
           paramMap.put("option_value",themeName);
           paramMap.put("type","0");

           sqlSession.insert("options.ioption",paramMap);
       }else{
           String value=(String)data.get("option_value");
           if(value.indexOf(themeName)>=0){
               //抛错错误
               throw new AlreadyExistsException("当前安装的主题已存在");
           }
           value=value+","+themeName;
           data.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
           data.put("option_value",value);
           data.put("siteid",Utils.getSiteIdFromMessage(message));
           data.put("option_key","installthemes");
           sqlSession.update("options.uoption",data);
       }
    }


        for(java.util.Iterator iter=zipMap.entrySet().iterator();iter.hasNext();){
            Map.Entry entry=(Map.Entry)iter.next();
            String path=(String)entry.getKey();
            byte[] bytes=(byte[])entry.getValue();
            Map paramMap=new HashMap();
            if(istext(path)){
                paramMap.put("content",new String(bytes,"UTF-8"));
            }else{
                paramMap.put("bcontent",bytes);
            }
            paramMap.put("lastModified",new java.sql.Timestamp(System.currentTimeMillis()));
            paramMap.put("path",path);
            paramMap.put("theme",themeName);
            paramMap.put("siteid", Utils.getSiteIdFromMessage(message));

            sqlSession.insert("dpress.itemplate",paramMap);

        }

        Map rtnMap=new HashMap();
        rtnMap.put("data",map);
         return rtnMap;
    }

    /**
     * 寻找基础路径
     * @param map
     * @return
     */
    public Map rmBaseMap(Map map){
        String indexfile="index.ftl";
        String path="";
        for(java.util.Iterator iter=map.entrySet().iterator();iter.hasNext();){
            Map.Entry entry=(Map.Entry)iter.next();
            int index=entry.getKey().toString().indexOf(indexfile);
            if(index>=0){
                path=entry.getKey().toString().substring(0,index);
                break;
            }
        }
        Map rtnMap=new HashMap();
        for(java.util.Iterator iter=map.entrySet().iterator();iter.hasNext();) {
            Map.Entry entry=(Map.Entry)iter.next();
            rtnMap.put(entry.getKey().toString().substring(path.length()),entry.getValue());

        }

            return rtnMap;
        }
    public static boolean istext(String path){
        path=path.toLowerCase();
        if(path.endsWith("ftl")){
            return true;
        }
        if(path.endsWith("js")){
            return true;
        }
        if(path.endsWith("css")){
            return true;
        }
        if(path.endsWith("md")){
            return true;
        }
        if(path.endsWith("yaml")){
            return true;
        }
        return false;
    }
    public Map getDataFromBytes(byte[] bytes) throws IOException {
        Map rtnMap=new HashMap();
        ByteArrayInputStream byteInputStream=new ByteArrayInputStream(bytes);
        ZipInputStream zis=new ZipInputStream(byteInputStream);
        ZipEntry entry = null;
        while ((entry = zis.getNextEntry()) != null) {
            if (entry.isDirectory()) { // is dir
                continue;
            }
            byte[] data = getByte(zis); // 获取当前条目的字节数组
            rtnMap.put(entry.getName(),data);
        }
        return rtnMap;
    }
    /**
     * 获取条目byte[]字节
     * @param zis
     * @return
     */
    public byte[] getByte(InflaterInputStream zis) {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] temp = new byte[1024];
            byte[] buf = null;
            int length = 0;

            while ((length = zis.read(temp, 0, 1024)) != -1) {
                bout.write(temp, 0, length);
            }

            buf = bout.toByteArray();
            bout.close();
            return buf;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
