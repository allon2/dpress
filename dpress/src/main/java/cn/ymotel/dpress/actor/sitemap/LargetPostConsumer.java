package cn.ymotel.dpress.actor.sitemap;

import cn.ymotel.largedatabtach.BatchDataConsumer;
import freemarker.core.Environment;
import freemarker.template.*;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class LargetPostConsumer  implements BatchDataConsumer {
    private SqlSession sqlSession;
    private Environment env;
    private TemplateDirectiveBody body;

    public Environment getEnv() {
        return env;
    }

    public void setEnv(Environment env) {
        this.env = env;
    }

    public TemplateDirectiveBody getBody() {
        return body;
    }

    public void setBody(TemplateDirectiveBody body) {
        this.body = body;
    }

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
        data=(Map)((List)obj).get(0);
    }

    @Override
    public void close() throws IOException {

    }

    private static Object obj =new Object();
        @Override
    public void run() {
        List list= sqlSession.selectList("posts.qbetweenids",data);
        if(list==null||list.size()==0){
            return;
        }
        String baseUrl=(String)data.get("baseUrl");
        String archives=(String)data.get("archives");
//            Environment env=(Environment)data.get("Environment");
//            TemplateDirectiveBody body=(TemplateDirectiveBody)data.get("TemplateDirectiveBody");

            synchronized (obj) {
                try {
                    final DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);

                    for(int i=0;i<list.size();i++){
                    Map tMap=(Map)list.get(i);
                    String url=baseUrl+"/"+archives+"/"+tMap.get("slug");
                    tMap.put("createTime",tMap.get("create_time"));
                    tMap.put("fullPath",url);
                        env.setVariable("post", builder.build().wrap(tMap));
                        body.render(env.getOut());
                }

                    env.getOut().flush();
                } catch (java.lang.Throwable e) {
                    e.printStackTrace();
                }
            }
        }
}
