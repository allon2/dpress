package cn.ymotel.dpress;

import cn.ymotel.dpress.service.SiteThemeService;
import org.apache.ibatis.session.SqlSession;
import org.yaml.snakeyaml.Yaml;
import run.halo.app.exception.AlreadyExistsException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ThemeZipUtils {
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


        for(java.util.Iterator iter=zipMap.entrySet().iterator();iter.hasNext();){
            Map.Entry entry=(Map.Entry)iter.next();
            String path=(String)entry.getKey();
            byte[] bytes=(byte[])entry.getValue();
            Map paramMap=new HashMap();
            if(SiteThemeService.istext(path)){
                paramMap.put("content",new String(bytes,"UTF-8"));
            }else{
                paramMap.put("bcontent",bytes);
            }
            paramMap.put("lastModified",new java.sql.Timestamp(System.currentTimeMillis()));
            paramMap.put("path",path);
            paramMap.put("theme",themeName);
            paramMap.put("siteid", siteid);
            sqlsession.insert("dpress.itemplate",paramMap);
        }
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
