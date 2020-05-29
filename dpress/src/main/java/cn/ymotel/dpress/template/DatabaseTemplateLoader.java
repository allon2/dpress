package cn.ymotel.dpress.template;

import freemarker.cache.NullCacheStorage;
import freemarker.cache.TemplateLoader;
import freemarker.core.TemplateClassResolver;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.apache.ibatis.session.SqlSession;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

import static freemarker.template.Configuration.AUTO_DETECT_TAG_SYNTAX;

/**
 * Created by xiaoj on 2016/9/8.
 */
 public class DatabaseTemplateLoader implements TemplateLoader {
        private SqlSession sqlSession;
        private long siteid;

    public long getSiteid() {
        return siteid;
    }

    public void setSiteid(long siteid) {
        this.siteid = siteid;
    }

    public SqlSession getSqlSession() {
        return sqlSession;
    }

    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public Object findTemplateSource(String name) {
        try {
            //通过id查询数据库中配置的模板信息
            Map map=new HashMap();
            map.put("path",name);
            map.put("siteid", siteid);

            Map themeMap=this.getSqlSession().selectOne("options.qactivetheme",map);
            String theme=(String)themeMap.get("option_value");
            map.put("theme",theme);
           Map rtnMap= this.getSqlSession().selectOne("dpress.qtemplate",map);
            String content=(String)rtnMap.get("content");
            long lastModified=((Date)rtnMap.get("lastModified")).getTime();

             //数据库表必须有一个最后更新字段用来刷新缓存,数据库中的模板保存字段为query,这里通过model.getQuery获取
            return new StringTemplateSource(name, content, lastModified);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public long getLastModified(Object templateSource) {
        return ((StringTemplateSource) templateSource).lastModified;
    }

    @Override
    public Reader getReader(Object templateSource, String encoding) {
        return new StringReader(((StringTemplateSource) templateSource).source);
    }
    public static Configuration getConfigurationInstance(TemplateLoader templateLoader) throws IOException, TemplateException {
//        DatabaseTemplateLoader loader = new DatabaseTemplateLoader();
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
//        configurer.setTemplateLoaderPaths("classpath:/templates/");
        configurer.setTemplateLoaderPaths("classpath:/templates/");
//        configurer.setPreferFileSystemAccess(false);
        configurer.setDefaultEncoding("UTF-8");
        configurer.setPostTemplateLoaders(templateLoader);
        Properties properties = new Properties();
        properties.setProperty("auto_import", "/common/macro/common_macro.ftl as common,/common/macro/global_macro.ftl as global");

        configurer.setFreemarkerSettings(properties);
        Map variable=new HashMap();
//        variable.put("extends",new cn.org.rapid_framework.freemarker.directive.ExtendsDirective());
//        variable.put("block",new cn.org.rapid_framework.freemarker.directive.BlockDirective());
//        variable.put("override",new cn.org.rapid_framework.freemarker.directive.OverrideDirective());
//        variable.put("super",new cn.org.rapid_framework.freemarker.directive.SuperDirective());

        configurer.setFreemarkerVariables(variable);
        // Predefine configuration
        Configuration templateConfig = configurer.createConfiguration();
//        Configuration templateConfig = new Configuration(Configuration.VERSION_2_3_29);
        templateConfig.setNewBuiltinClassResolver(TemplateClassResolver.SAFER_RESOLVER);

        templateConfig.setLocalizedLookup(false);
//        templateConfig.setTemplateLoader(loader);
        templateConfig.setDefaultEncoding("UTF-8");
        templateConfig.setOutputEncoding("UTF-8");
        templateConfig.setLocale(Locale.CHINA);
        templateConfig.setDateFormat("yyyy-MM-dd");
        templateConfig.setTimeFormat("HH:mm:ss");
        templateConfig.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
        templateConfig.setNumberFormat("#");
        /**
         * 不缓存
         */
        templateConfig.setCacheStorage(NullCacheStorage.INSTANCE);
//        templateConfig.setClassicCompatible(true);
//        templateConfig.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);

//        templateConfig.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        templateConfig.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        templateConfig.setTemplateUpdateDelayMilliseconds(0);

//        templateConfig.setTemplateUpdateDelayMilliseconds(60*1000);
        templateConfig.setTagSyntax(AUTO_DETECT_TAG_SYNTAX);
        return templateConfig;

//
//        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
//        configurer.setTemplateLoaderPaths("file://f:/tmp/ftl/", "classpath:/templates/");
//        configurer.setDefaultEncoding("UTF-8");
//
//        Properties properties = new Properties();
//        properties.setProperty("auto_import", "/common/macro/common_macro.ftl as common,/common/macro/global_macro.ftl as global");
//
//        configurer.setFreemarkerSettings(properties);
//
//        // Predefine configuration
//        freemarker.template.Configuration configuration = configurer.createConfiguration();
//
//        configuration.setNewBuiltinClassResolver(TemplateClassResolver.SAFER_RESOLVER);
//
//
//            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);


//        return configuration;
    }
    @Override
    public void closeTemplateSource(Object templateSource) {
        //do nothing
    }
    public static void main(String[] args) throws IOException, TemplateException {
        DatabaseTemplateLoader loader = new DatabaseTemplateLoader();

        Configuration templateConfig = new Configuration(Configuration.VERSION_2_3_29);
        templateConfig.setLocalizedLookup(false);
        templateConfig.setTemplateLoader(loader);
        Template temp =templateConfig.getTemplate("/a/b.html");
        StringWriter writer = new StringWriter();
        Map<String, Object> delegate=new HashMap();
        temp.process(delegate, writer);
        String query = writer.toString();
        writer.close();
        System.out.println(query);
    }
    private static class StringTemplateSource {
        private final String name;
        private final String source;
        private final long lastModified;

        StringTemplateSource(String name, String source, long lastModified) {
            if (name == null) {
                throw new IllegalArgumentException("name == null");
            }
            if (source == null) {
                throw new IllegalArgumentException("source == null");
            }
            if (lastModified < -1L) {
                throw new IllegalArgumentException("lastModified < -1L");
            }
            this.name = name;
            this.source = source;
            this.lastModified = lastModified;
        }

        public boolean equals(Object obj) {
            if (obj instanceof StringTemplateSource) {
                return name.equals(((StringTemplateSource) obj).name);
            }
            return false;
        }

        public int hashCode() {
            return name.hashCode();
        }
    }

    @Override
    public String toString() {
        return "DatabaseTemplateLoader(db=\"db_stat\", table=\"DMS_MD_TEMPLATE_CONFIG\")";
    }


}