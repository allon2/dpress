package cn.ymotel.dpress.actor.sitemap;

import cn.ymotel.largedatabtach.BatchDataConsumer;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Consumer implements BatchDataConsumer {
    private SqlSession sqlSession;

    public SqlSession getSqlSession() {
        return sqlSession;
    }

    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    private Map data;
    @Override
    public void setData(Object obj) {
        if(obj==null){
            data=null;
            return ;
        }
//        System.out.println(obj);
        data=(Map)((List)obj).get(0);
    }

    @Override
    public void close() throws IOException {

    }
    private String getnewslinkxml(String url, Date time,java.text.SimpleDateFormat sdf){

        return "<url>\n" +
                " <loc>"+url+"</loc>\n" +
                "<lastmod>"+sdf.format(time)+"</lastmod>\n" +
                "</url>\n";

    }
    public static synchronized void WriteString(PrintWriter writer,String out){
        writer.write(out);
        writer.flush();
    }
    @Override
    public void run() {
       List list= sqlSession.selectList("posts.qlimitbynormal",data);
       if(list==null||list.size()==0){
           return;
       }
        java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyy-MM-dd");

        String baseUrl=(String)data.get("baseUrl");
       String archives=(String)data.get("archives");
        PrintWriter writer=(PrintWriter)data.get("writer");
        StringBuilder sb=new StringBuilder();
       for(int i=0;i<list.size();i++){
           Map tMap=(Map)list.get(i);
           String url=baseUrl+"/"+archives+"/"+tMap.get("slug");
           sb.append(getnewslinkxml(url,(java.util.Date)tMap.get("create_time"),sdf));
       }
        WriteString(writer,sb.toString());
    }
}
