package cn.ymotel.dpress.template;

import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.actor.OptionsService;
import freemarker.template.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import run.halo.app.core.freemarker.tag.*;
import run.halo.app.handler.theme.config.support.ThemeProperty;
import run.halo.app.model.properties.BlogProperties;
import run.halo.app.model.properties.SeoProperties;
import run.halo.app.model.support.HaloConst;
import run.halo.app.service.OptionService;
import run.halo.app.service.ThemeService;
import run.halo.app.service.ThemeSettingService;
import run.halo.app.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Component
public class MultiDomainFreeMarkerView implements ApplicationContextAware {
    private Map<Object,Configuration> templateConfigMap=new ConcurrentHashMap();
    @Autowired
    private SqlSession sqlSession;
    @Autowired
    private  ThemeService themeService;
    @Autowired
    private  ThemeSettingService themeSettingService;
    @Autowired
    private  UserService userService;
    @Autowired
    private  OptionService optionService;

    @Autowired
    private OptionsService optionsService;


    public Configuration getConfiguration(ServletMessage message,Object id) throws IOException, TemplateException {

        Configuration config= templateConfigMap.get(id+"");
        if(config==null){
            config=createTemplateConfig((long)id);
            config.setSharedVariable("_siteid",new SimpleNumber((long)id));
            String url= Utils.getBaseUrl(message);
            config.setSharedVariable("_baseurl",new SimpleScalar(url));
            setSharedVariables(config,url,id);
            setVariables(config);
            setBaseUrlVariables(config,url);
            templateConfigMap.put(id+"",config);
        }
        return config;
    }
    public String getProcessedString(ServletMessage message,Object id,String viewName,Map data) throws IOException, TemplateException {
        Configuration configuration= getConfiguration(message,id);
        Template temp =  configuration.getTemplate(viewName+".ftl");
        StringWriter writer = new StringWriter();
        Map<String, Object> delegate=new HashMap();
        delegate.putAll(data);
        temp.process(delegate, writer);
        String content = writer.toString();
        writer.close();
        return content;
    }

//    public   String getBaseUrl(ServletMessage message){
//        HttpServletRequest request= message.getRequest();
//        String path = request.getContextPath();
//
//        if(!optionService.isEnabledAbsolutePath()){
//            return path;
//        }
//
//        String scheme=request.getScheme();
//        String baseUrl= request.getScheme()+"://"+request.getServerName();
//        if(scheme.equalsIgnoreCase("http")){
//            if(request.getServerPort()==80){
//
//            }else{
//                baseUrl=baseUrl+":"+request.getServerPort();
//            }
//            baseUrl=baseUrl+path;
//            return baseUrl;
//        }
//        if(scheme.equalsIgnoreCase("https")){
//            if(request.getServerPort()==443){
//
//            }else{
//                baseUrl=baseUrl+":"+request.getServerPort();
//            }
//            baseUrl=baseUrl+path;
//            return baseUrl;
//        }
//        return "";
//    }
    public Configuration createTemplateConfig(long siteid) throws IOException, TemplateException {
        DatabaseTemplateLoader loader = new DatabaseTemplateLoader();
        loader.setSqlSession(sqlSession);
        loader.setSiteid(siteid);
        Configuration templateConfig = DatabaseTemplateLoader.getConfigurationInstance(loader);
//        templateConfig.setTemplateLoader(loader);
        return templateConfig;
    }
    public void setSharedVariables(Configuration configuration,String baseurl,Object id) throws TemplateModelException {
        loadUserConfig(configuration);
        loadOptionsConfig(configuration,baseurl,id);
        loadThemeConfig(configuration,baseurl);
    }
    public void setVariables(Configuration configuration){

        configuration.setSharedVariable("categoryTag", applicationContext.getBean(CategoryTagDirective.class));
        configuration.setSharedVariable("commentTag", applicationContext.getBean(CommentTagDirective.class));
        configuration.setSharedVariable("linkTag", applicationContext.getBean(LinkTagDirective.class));
        configuration.setSharedVariable("menuTag", applicationContext.getBean(MenuTagDirective.class));
        configuration.setSharedVariable("paginationTag", applicationContext.getBean(PaginationTagDirective.class));
        configuration.setSharedVariable("photoTag", applicationContext.getBean(PhotoTagDirective.class));
        configuration.setSharedVariable("postTag", applicationContext.getBean(PostTagDirective.class));
        configuration.setSharedVariable("tagTag", applicationContext.getBean(TagTagDirective.class));
        configuration.setSharedVariable("toolTag", applicationContext.getBean(ToolTagDirective.class));
    }
    private void loadUserConfig(Configuration configuration) throws TemplateModelException {
        configuration.setSharedVariable("user", userService.getCurrentUser().orElse(null));
    }

    private void loadOptionsConfig(Configuration configuration,String baseurl,Object id) throws TemplateModelException {

        String context =  optionsService.getBooleanOption(id,OptionsService.GLOBAL_ABSOLUTE_PATH_ENABLED,true) ? baseurl + "/" : "/";

//        configuration.setSharedVariable("options", optionService.listOptions());
        configuration.setSharedVariable("options", optionsService.getOptions(id));
        configuration.setSharedVariable("context", context);
        configuration.setSharedVariable("version", HaloConst.HALO_VERSION);

        configuration.setSharedVariable("blog_title", optionsService.getOption(id,OptionsService.BLOG_TITLE,""));
        configuration.setSharedVariable("blog_url", baseurl);
//        configuration.setSharedVariable("blog_logo", optionService.getByPropertyOrDefault(BlogProperties.BLOG_LOGO, String.class, BlogProperties.BLOG_LOGO.defaultValue()));
        configuration.setSharedVariable("blog_logo", optionsService.getOption(id,OptionsService.BLOG_LOGO,""));
        configuration.setSharedVariable("seo_keywords",optionsService.getOption(id,OptionsService.KEY_WORDS,""));
//        configuration.setSharedVariable("seo_keywords", optionService.getByPropertyOrDefault(SeoProperties.KEYWORDS, String.class, SeoProperties.KEYWORDS.defaultValue()));
        configuration.setSharedVariable("seo_description",optionsService.getOption(id,OptionsService.DESCRIPTION,""));
//        configuration.setSharedVariable("seo_description", optionService.getByPropertyOrDefault(SeoProperties.DESCRIPTION, String.class, SeoProperties.DESCRIPTION.defaultValue()));

        configuration.setSharedVariable("rss_url", baseurl + "/rss.xml");
        configuration.setSharedVariable("atom_url", baseurl + "/atom.xml");
        configuration.setSharedVariable("sitemap_xml_url", baseurl + "/sitemap.xml");
        configuration.setSharedVariable("sitemap_html_url", baseurl + "/sitemap.html");
        configuration.setSharedVariable("links_url",context + optionsService.getOption(id,OptionsService.LINKS_PREFIX,"links"));
//        configuration.setSharedVariable("links_url", context + optionService.getLinksPrefix());
//        configuration.setSharedVariable("photos_url", context + optionService.getPhotosPrefix());
        configuration.setSharedVariable("photos_url", context + optionsService.getOption(id,OptionsService.PHOTOS_PREFIX,"photos"));
//        configuration.setSharedVariable("journals_url", context + optionService.getJournalsPrefix());
        configuration.setSharedVariable("journals_url", context + optionsService.getOption(id,OptionsService.JOURNALS_PREFIX,"journals"));
//        configuration.setSharedVariable("archives_url", context + optionService.getArchivesPrefix());
        configuration.setSharedVariable("archives_url", context + optionsService.getOption(id,OptionsService.ARCHIVES_PREFIX,"archives"));
//        configuration.setSharedVariable("categories_url", context + optionService.getCategoriesPrefix());
        configuration.setSharedVariable("categories_url", context + optionsService.getOption(id,OptionsService.CATEGORIES_PREFIX,"categories"));
//        configuration.setSharedVariable("tags_url", context + optionService.getTagsPrefix());
        configuration.setSharedVariable("tags_url", context + optionsService.getOption(id,OptionsService.TAGS_PREFIX,"tags"));

    }

    public Map getSiteInfo(Object id){
        Map map=new HashMap();
        map.put("id",id);
        return sqlSession.selectOne("dpress.qsiteidbyid",map);
    }

    private void setBaseUrlVariables(Configuration configuration , String baseUrl) throws TemplateModelException {
        ThemeProperty activatedTheme = themeService.getActivatedTheme();

        String themeBasePath = baseUrl + "/themes/" + activatedTheme.getFolderName();

        configuration.setSharedVariable("static", themeBasePath);

        configuration.setSharedVariable("theme_base", themeBasePath);
    }
    private void loadThemeConfig(Configuration configuration,String baseurl) throws TemplateModelException {

        // Get current activated theme.
        ThemeProperty activatedTheme = themeService.getActivatedTheme();

        String themeBasePath = (optionService.isEnabledAbsolutePath() ? baseurl : "") + "/themes/" + activatedTheme.getFolderName();

        configuration.setSharedVariable("theme", activatedTheme);

//        configuration.setSharedVariable("static", themeBasePath);
//
//        configuration.setSharedVariable("theme_base", themeBasePath);

        configuration.setSharedVariable("settings", themeSettingService.listAsMapBy(themeService.getActivatedThemeId()));
    }
    private ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext= applicationContext;
    }
}
