package cn.ymotel.dpress.admin.install;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.sequence.IdWorker;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.ThemeZipUtils;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;
import run.halo.app.exception.BadRequestException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@ActorCfg(urlPatterns = "/api/admin/installations",chain = "publicchain",timeout = 60000)
public class InstallSiteActor implements Actor<ServletMessage> {
    @Override
    public Object Execute(ServletMessage message) throws Throwable {
        long begin=System.currentTimeMillis();
        if(Utils.isInstall()){
//            throw new BadRequestException("该博客已初始化，不能再次安装！");

        }else{
            InstallDbScript(message.getContext());

        }
        if(!emptySite()){
            throw new BadRequestException("该博客已初始化，不能再次安装！");
        }
        long siteid= IdWorker.getInstance().nextId();
        Map params=message.getContext();
        String serverName=getServerName((String)params.get("url"));
        String title=(String)params.get("title");
        createSiteInfo(siteid,serverName,title);
        initSetting(siteid,message.getContext());
        createUser(siteid,params);
        createCategories(siteid);
        Object postid=createDefaultPost(siteid);
        createDefaultComment(siteid,postid);
        createDefaultSheet(siteid);
        createDefaultMenu(siteid);
        InstallDefaultThemes(siteid);
        System.out.println("安装完成！");
        message.getAsyncContext().getResponse().setContentType("text/html; charset=UTF-8");
        message.getAsyncContext().getResponse().getWriter().write("安装完成！");
        message.getAsyncContext().getResponse().getWriter().flush();
        message.getAsyncContext().complete();
        System.out.println(System.currentTimeMillis()-begin);
        return "安装完成！";
    }
    public void InstallDbScript(Map params) throws IOException {
        String dbhost=(String)params.get("dbhost");
        String dbname=(String)params.get("dbname");
        String dbpassword=(String)params.get("dbpassword");
        String dbport=(String)params.get("dbport");
        String dbusername=(String)params.get("dbusername");
        String url="jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
        Flyway flyway = Flyway
            .configure()
            .locations("classpath:/migration")
            .baselineVersion("1")
            .baselineOnMigrate(true)
            .dataSource(url, dbusername, dbpassword)
            .load();
        flyway.repair();
        flyway.migrate();
        Utils.WriteMysqlInfo(url,dbusername,dbpassword);
    }
    public void InstallDefaultThemes(Object siteid) throws IOException {
        String path="classpath:/themes/*.zip";
           Resource[] rs = ResourcePatternUtils
                .getResourcePatternResolver(new DefaultResourceLoader()).getResources(path);
         List<CompletableFuture>  futureList=new ArrayList<>();
        for(int i=0;i<rs.length;i++){
           final Resource t=rs[i];
            CompletableFuture future1= CompletableFuture.runAsync(() -> {
                Map map= null;
                try {
                    map = ThemeZipUtils.installTheme(sqlSession,t.getInputStream(),siteid);
                } catch (IOException e) {
                    e.printStackTrace();
                    return ;
                }
                if(map.get("id").equals("codelunatic_simple")){
            //
            Map settingMap=new HashMap();
            settingMap.put("siteid",siteid);
            settingMap.put("create_time",new java.sql.Timestamp(System.currentTimeMillis()));
            settingMap.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
            settingMap.put("type","0");
            {
                settingMap.put("option_key","theme");
                settingMap.put("option_value","codelunatic_simple");
                sqlSession.insert("options.ioption",settingMap);
            }
        }
            });
            futureList.add(future1);
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
    }
    public  String getServerName(String url) throws MalformedURLException {

        URL url2=new URL(url);
        return url2.getHost();
    }

    @Autowired
    private SqlSession sqlSession;

    public boolean emptySite(){
        Map siteMap=sqlSession.selectOne("dpress_siteinfo.qsitenumber");
        Long ct=(Long)siteMap.get("ct");
        if(ct==0){
            return true;
        }else{
            return false;
        }
    }
    public void createSiteInfo(Object siteid,String domain,String sitename){
        Map map=new HashMap();
        map.put("id",siteid);
        map.put("domain",domain);
        map.put("sitename",sitename);
        this.sqlSession.insert("dpress_siteinfo.i",map);
    }
    public void createDefaultMenu(Object siteid){

        Map map = new HashMap();
        map.put("siteid", siteid);
        map.put("create_time",new java.sql.Timestamp(System.currentTimeMillis()));
        map.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));

        {

            map.put("name", "首页");
            map.put("url", "/");
            map.put("priority",1);
            map.put("target","_self");
            map.put("parent_id",0);
            sqlSession.insert("menus.i",map);
        }
        {

            map.put("name", "文章归档");
            map.put("url", "/archives");
            map.put("priority",2);
            map.put("target","_self");
            map.put("parent_id",0);
            sqlSession.insert("menus.i",map);
        }
        {

            map.put("name", "默认分类");
            map.put("url", "/categories/default");
            map.put("priority",3);
            map.put("target","_self");
            map.put("parent_id",0);
            sqlSession.insert("menus.i",map);
        }
        {

            map.put("name", "关于页面");
            map.put("url", "/s/about");
            map.put("priority",4);
            map.put("target","_self");
            map.put("parent_id",0);
            sqlSession.insert("menus.i",map);
        }

    }
    public void createDefaultComment(Object siteid,Object postid){
        Map map=new HashMap();
        map.put("siteid",siteid);
        map.put("create_time",new java.sql.Timestamp(System.currentTimeMillis()));
        map.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
        map.put("type","0");
        map.put("author","Halo");
        map.put("author_url","https://github.com/allon2/dpress");
        map.put("content","欢迎使用 Halo，这是你的第一条评论，头像来自 [Gravatar](https://cn.gravatar.com)，你也可以通过注册 [Gravatar](https://cn.gravatar.com) 来显示自己的头像。");
        map.put("email","hi@dpress.com");
        map.put("status","1");
        map.put("post_id",postid);
        sqlSession.insert("comments.i",map);

    }
    public void createDefaultSheet(Object siteid){
        Map map=new HashMap();
        map.put("siteid",siteid);
        map.put("create_time",new java.sql.Timestamp(System.currentTimeMillis()));
        map.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
        map.put("type","1");
        map.put("slug","about");
        map.put("title","关于页面");
        map.put("status","0");
        map.put("original_content","## 关于页面\n" +
                "\n" +
                "这是一个自定义页面，你可以在后台的 `页面` -> `所有页面` -> `自定义页面` 找到它，你可以用于新建关于页面、留言板页面等等。发挥你自己的想象力！\n" +
                "\n" +
                " 这是一篇自动生成的页面，你可以在后台删除它。");
        map.put("edit_time",new java.sql.Timestamp(System.currentTimeMillis()));
//        map.put("format_content",map.get("original_content"));
        map.put("editor_type","0");
        map.put("likes","0");
        map.put("disallow_comment",false);
        map.put("visits",0);

        sqlSession.insert("posts.i",map);

    }
    public  Object createDefaultPost(Object siteid){
        Map map=new HashMap();
        map.put("slug","hello-dpress");
        map.put("title","Hello Dpress");
        map.put("status","0");
        map.put("original_content","## Hello Dpress  \n  " +
                "\n" +
                "如果你看到了这一篇文章，那么证明你已经安装成功了，感谢使用 [Dpress](https://github.com/allon2/dpress) 进行创作，希望能够使用愉快。  \n" +
                "\n" +
                "## 相关链接  \n" +
                "\n" +
                "- 官网：[https://github.com/allon2/dpress](https://github.com/allon2/dpress)  \n" +
                 "\n" +
                "在使用过程中，有任何问题都可以通过以上链接找寻答案，或者联系我们。  \n" +
                "\n" +
                "> 这是一篇自动生成的文章，请删除这篇文章之后开始你的创作吧！  \n" +
                "\n");
        map.put("siteid",siteid);
//        map.put("format_content",map.get("original_content"));
        map.put("editor_type","0");
        map.put("likes","0");
        map.put("create_time",new java.sql.Timestamp(System.currentTimeMillis()));
        map.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
        map.put("edit_time",new java.sql.Timestamp(System.currentTimeMillis()));
        map.put("type","0");
        map.put("visits",0);
        map.put("disallow_comment",false);
        sqlSession.insert("posts.i",map);

        Map rtnMap=sqlSession.selectOne("posts.qbyslug",map);
        return rtnMap.get("id");

    }
    public void createCategories(Object siteid){
        Map map=new HashMap();
        map.put("name","默认分类");
        map.put("slug","default");
        map.put("description","这是你的默认分类，如不需要，删除即可。");
        map.put("create_time",new java.sql.Timestamp(System.currentTimeMillis()));
        map.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
        map.put("siteid",siteid);
        map.put("parent_id",0);
        sqlSession.insert("categories.i",map);

    }
    public void createUser(Object siteid,Map params){
        String password= BCrypt.hashpw((String)params.get("password"), BCrypt.gensalt());
        String gravatar = "//cn.gravatar.com/avatar/" + SecureUtil.md5((String)params.get("email")) + "?s=256&d=mm";
        Map userMap=new HashMap();
        userMap.put("create_time",new java.sql.Timestamp(System.currentTimeMillis()));
        userMap.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
        userMap.put("avatar",gravatar);
        userMap.put("description",params.get("description"));
        userMap.put("email",params.get("email"));
        userMap.put("expire_time",params.get("expire_time"));
        userMap.put("nickname",params.get("nickname"));
        userMap.put("password",password);
        userMap.put("username",params.get("username"));
        userMap.put("siteid",siteid);
        sqlSession.insert("users.i",userMap);
    }
    public void initSetting(Object siteid,Map params){
        Map settingMap=new HashMap();
        settingMap.put("siteid",siteid);
        settingMap.put("create_time",new java.sql.Timestamp(System.currentTimeMillis()));
        settingMap.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
        settingMap.put("type","0");
        {
            settingMap.put("option_key","is_installed");
            settingMap.put("option_value","true");
            sqlSession.insert("options.ioption",settingMap);
        }
        {
            settingMap.put("option_key","blog_locale");
            settingMap.put("option_value",params.getOrDefault("locale","zh"));
            sqlSession.insert("options.ioption",settingMap);
        }
        {
            settingMap.put("option_key","blog_title");
            settingMap.put("option_value",params.get("title"));
            sqlSession.insert("options.ioption",settingMap);
        }
        {

            settingMap.put("option_key","blog_url");
            settingMap.put("option_value",params.getOrDefault("url",""));
            sqlSession.insert("options.ioption",settingMap);
        }
        {
            settingMap.put("option_key","global_absolute_path_enabled");
            settingMap.put("option_value","false");
            sqlSession.insert("options.ioption",settingMap);
        }
        {
            settingMap.put("option_key","birthday");
            settingMap.put("option_value",params.getOrDefault("birthday",String.valueOf(System.currentTimeMillis())));
            sqlSession.insert("options.ioption",settingMap);
        }
    }
}
