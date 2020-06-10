package cn.ymotel.dpress;

import cn.ymotel.dpress.entity.mapper.SystemThemesMapper;
import cn.ymotel.dpress.entity.model.SystemThemes;
import cn.ymotel.dpress.service.SiteThemeService;
import org.apache.any23.encoding.TikaEncodingDetector;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.tika.Tika;
import org.apache.tika.detect.AutoDetectReader;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;
import org.apache.tika.parser.txt.UniversalEncodingDetector;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.yaml.snakeyaml.Yaml;
import run.halo.app.exception.AlreadyExistsException;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ThemeZipUtils {
    public static void installSystemTheme(SqlSession sqlSession,InputStream in) throws IOException {
        Map zipMap = ThemeZipUtils.getDataFromBytes(in);
        zipMap = ThemeZipUtils.rmBaseMap(zipMap);

        byte[] yamlbytes = (byte[]) zipMap.get("theme.yaml");
        Yaml yaml = new Yaml();
        Map map =  yaml.load(new ByteArrayInputStream(yamlbytes));
        String themeName = (String) map.get("id");
        zipMap.forEach((BiConsumer<String, byte[]>) (path, bytes) -> {
             String mediaType=getMediaType(bytes,path);
            SystemThemes systemThemes=new SystemThemes();
            systemThemes.setMediatype(mediaType);

            if(SiteThemeService.istext(path)){
                try {
                    String encoding=getEncoding(bytes,"UTF-8");
                    systemThemes.setEncoding(encoding);
                    systemThemes.setContent(new String(bytes,encoding));

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }else{
                systemThemes.setBcontent(bytes);
            }
            systemThemes.setPath(path);
            systemThemes.setTheme(themeName);
            sqlSession.getMapper(SystemThemesMapper.class).insert(systemThemes);
//            paramMap.put("path",path);
//
//            paramMap.put("lastModified",new java.sql.Timestamp(System.currentTimeMillis()));
//            paramMap.put("path",path);
//            paramMap.put("theme",themeName);
//            paramMap.put("siteid", siteid);
//            sqlsession.insert("dpress.itemplate",paramMap);
        });
    }
    public static Map installTheme(SqlSession sqlsession, InputStream in, Object siteid) throws IOException {
        Map zipMap = ThemeZipUtils.getDataFromBytes(in);
        zipMap = ThemeZipUtils.rmBaseMap(zipMap);

        byte[] yamlbytes = (byte[]) zipMap.get("theme.yaml");
        Yaml yaml = new Yaml();
        Map map =  yaml.load(new ByteArrayInputStream(yamlbytes));

        String themeName = (String) map.get("id");
        {
            Map paramMap=new HashMap();
            paramMap.put("siteid", siteid);
            paramMap.put("key","installthemes");
            Map data= sqlsession.selectOne("options.qoption",paramMap);
            if(data==null||data.isEmpty()){
                //
                paramMap.put("create_time",new java.sql.Timestamp(System.currentTimeMillis()));
                paramMap.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
                paramMap.put("option_key","installthemes");
                paramMap.put("option_value",themeName);
                paramMap.put("type","0");

                sqlsession.insert("options.ioption",paramMap);
            }else{
                String value=(String)data.get("option_value");
                if(value.indexOf(themeName)>=0){
                    //抛错错误
                    throw new AlreadyExistsException("当前安装的主题已存在");
                }
                value=value+","+themeName;
                data.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
                data.put("option_value",value);
                data.put("siteid",siteid);
                data.put("option_key","installthemes");
                sqlsession.update("options.uoption",data);
            }
        }

        zipMap.forEach((BiConsumer<String, byte[]>) (path, bytes) -> {
            Map paramMap=new HashMap();
            String mediaType=getMediaType(bytes,path);

            paramMap.put("mediatype",mediaType);

            if(SiteThemeService.istext(path)){
                try {
                    String encoding=getEncoding(bytes,"UTF-8");
                    paramMap.put("encoding",encoding);
                    paramMap.put("content",new String(bytes,encoding));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }else{
                paramMap.put("bcontent",bytes);
            }
            paramMap.put("path",path);

            paramMap.put("lastModified",new java.sql.Timestamp(System.currentTimeMillis()));
            paramMap.put("path",path);
            paramMap.put("theme",themeName);
            paramMap.put("siteid", siteid);
            sqlsession.insert("dpress.itemplate",paramMap);
        });
//        for(java.util.Iterator iter=zipMap.entrySet().iterator();iter.hasNext();){
//            Map.Entry entry=(Map.Entry)iter.next();
//            String path=(String)entry.getKey();
//            byte[] bytes=(byte[])entry.getValue();
//            Map paramMap=new HashMap();
//            String mediaType=getMediaType(bytes,path);
//            String encoding=getEncoding(bytes,"UTF-8");
//            paramMap.put("encoding",encoding);
//            paramMap.put("mediatype",mediaType);
//
//            if(SiteThemeService.istext(path)){
//                paramMap.put("content",new String(bytes,encoding));
//            }else{
//                paramMap.put("bcontent",bytes);
//            }
//            paramMap.put("path",path);
//
//            paramMap.put("lastModified",new java.sql.Timestamp(System.currentTimeMillis()));
//            paramMap.put("path",path);
//            paramMap.put("theme",themeName);
//            paramMap.put("siteid", siteid);
//            sqlsession.insert("dpress.itemplate",paramMap);
//        }
        return map;

    }
    /**
     * 寻找基础路径
     * @param map
     * @return
     */
    public static  Map rmBaseMap(Map map){
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
    public static String getMediaType(byte[] bs,String fileName){
        MediaType mediaType=null;
        mediaType = MediaTypeFactory.getMediaType(fileName).orElse(null);
        if(mediaType==null){
            return  null;
        }
       return mediaType.toString();
    }

    public static Map getDataFromBytes(InputStream in) throws IOException {
        Map rtnMap=new HashMap();
//        ByteArrayInputStream byteInputStream=new ByteArrayInputStream(bytes);
        ZipInputStream zis=new ZipInputStream(in);
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
    public static String getEncoding(byte[] bs,String defaultCharset)  {
        TikaEncodingDetector detector = new TikaEncodingDetector();
        try {
            String guessEncoding = detector.guessEncoding(new BufferedInputStream(new ByteArrayInputStream(bs)));
            return  guessEncoding;
        } catch (java.lang.Throwable e) {
            e.printStackTrace();
            return defaultCharset;
        }

    }
    public static void main(String[] args) throws IOException, TikaException {
        System.out.println(getMediaType(null,"a.jpg"));
////        FileInputStream fileInputStream=new FileInputStream();
//        byte[] bs=FileUtils.readFileToByteArray(new File("F:\\tmp\\ftl\\source\\plugins\\prism\\prism.css"));
////        Tika tika=new Tika();
////        System.out.println(tika.detect(fileInputStream,"q1.sql").);
//
//
//        {
//            TikaEncodingDetector detector = new TikaEncodingDetector();
//            String guessEncoding = detector.guessEncoding(new BufferedInputStream(new ByteArrayInputStream(bs)));
//            System.out.println(guessEncoding);
//        }
//        {
//            CharsetDetector detector = new CharsetDetector();
//            detector.setText(new ByteArrayInputStream(bs));
//
//            CharsetMatch cm = detector.detect();
//            System.out.println(cm.getName());
//        }
//
////        UniversalEncodingDetector detector=new UniversalEncodingDetector();
////        System.out.println(detector.detect(new BufferedInputStream(new ByteArrayInputStream(bs)),new Metadata()));
//
////        System.out.println(detector.detect(new BufferedInputStream(fileInputStream),new Metadata()));
//        System.out.println();
////        AutoDetectReader autoDetectReader=new AutoDetectReader(fileInputStream) ;
////        System.out.println(autoDetectReader.getCharset());
    }
    /**
     * 获取条目byte[]字节
     * @param zis
     * @return
     */
    public static byte[] getByte(InflaterInputStream zis) {
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
