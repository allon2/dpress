package run.halo.app;

import cn.ymotel.dactor.ActorUtils;
import cn.ymotel.dactor.core.ActorChainCfg;
import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.DispatcherServlet;
import run.halo.app.repository.base.BaseRepositoryImpl;

import javax.servlet.DispatcherType;

/**
 * Halo main class.
 *
 * @author ryanwang
 * @date 2017-11-14
 */
@SpringBootApplication(scanBasePackages = {"run.halo.app","cn.ymotel.dpress"},exclude = {DataSourceAutoConfiguration.class, FlywayAutoConfiguration.class})
//@SpringBootApplication(scanBasePackages = {"run.halo.app","cn.ymotel.dpress"})
@EnableJpaAuditing
@EnableScheduling
//@EnableAsync
@EnableJpaRepositories(basePackages = "run.halo.app.repository", repositoryBaseClass = BaseRepositoryImpl.class)
@EnableMethodCache(basePackages ={"run.halo.app","cn.ymotel.dpress"})
@EnableCreateCacheAnnotation
//@PropertySource(value = {"classpath*:/conf/**.properties","classpath*:/conf/**.yml", "classpath*:/conf/**.yaml"},factory = CompositePropertySourceFactory.class)
@Lazy
public class Application extends SpringBootServletInitializer {

    private static ConfigurableApplicationContext CONTEXT;
    @Autowired
    private ApplicationContext applicationContext=null;
    public static void main(String[] args) {
        // Customize the spring config location
//        System.setProperty("spring.config.additional-location", "file:${user.home}/.halo/,file:${user.home}/halo-dev/");
        additionLocation();
        SpringApplication springApplication=new SpringApplication(Application.class);
//        springApplication.setResourceLoader();
//        springApplication.setDefaultProperties(loadProperties());
        // Run application
        CONTEXT = springApplication.run(Application.class, args);

    }
//    @Bean
//    public ServletRegistrationBean createServlet(){
//        ServletRegistrationBean servletRegistrationBean=new ServletRegistrationBean();
//        servletRegistrationBean.setServlet(new DemoServlet());
//        servletRegistrationBean.addUrlMappings("/*");
//        return servletRegistrationBean;
//    }
//    @Bean
//    public FilterRegistrationBean create404Filer(){
//        Error404Filter filter=new Error404Filter();
//        FilterRegistrationBean filterRegistrationBean=new FilterRegistrationBean();
//        filterRegistrationBean.setFilter(filter);
//        filterRegistrationBean.setDispatcherTypes(DispatcherType.ERROR);
//        filterRegistrationBean.addUrlPatterns("/*");
//        filterRegistrationBean.setOrder(100);
////        filterRegistrationBean.setAsyncSupported(true);
//        return filterRegistrationBean;
//    }
    @Bean(name="publicchain")
    public ActorChainCfg creatDefaultChain(){
        return  ActorUtils.creatDefaultChain(applicationContext,"publicchain","TransportResponseViewActor");
    }
    @Bean(name="errorchain")
    public ActorChainCfg creatErrorChain(){
        return  ActorUtils.creatDefaultChain(applicationContext,"errorchain",null);
    }
//    public Map
    public static void additionLocation(){
        ApplicationHome applicationHome=new ApplicationHome(Application.class);
        String parent=applicationHome.getSource().getParent();
        parent=parent+ "/application.yaml";
        System.setProperty("spring.config.additional-location", "file:${user.home}/.halo/,file:${user.home}/halo-dev/,file:"+parent);

    }
//    @Bean
//    public ServletRegistrationBean servletRegistrationBean(final DispatcherServlet dispatcherServlet) {
//        final ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(dispatcherServlet);
//        servletRegistrationBean.setEnabled(false);
//        return servletRegistrationBean;
//    }
    /**
     *
     * Restart Application.
     */
    public static void restart() {
        ApplicationArguments args = CONTEXT.getBean(ApplicationArguments.class);

        Thread thread = new Thread(() -> {
            CONTEXT.close();
            CONTEXT = SpringApplication.run(Application.class, args.getSourceArgs());
        });

        thread.setDaemon(false);
        thread.start();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        System.setProperty("spring.config.additional-location", "file:${user.home}/.halo/,file:${user.home}/halo-dev/");
        additionLocation();
        return application.sources(Application.class);
    }
}
