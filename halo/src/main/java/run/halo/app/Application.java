package run.halo.app;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import run.halo.app.repository.base.BaseRepositoryImpl;

/**
 * Halo main class.
 *
 * @author ryanwang
 * @date 2017-11-14
 */
@SpringBootApplication(scanBasePackages = {"run.halo.app","cn.ymotel.dpress"})
@EnableJpaAuditing
@EnableScheduling
//@EnableAsync
@EnableJpaRepositories(basePackages = "run.halo.app.repository", repositoryBaseClass = BaseRepositoryImpl.class)
@EnableMethodCache(basePackages ={"run.halo.app","cn.ymotel.dpress"})
@EnableCreateCacheAnnotation
public class Application extends SpringBootServletInitializer {

    private static ConfigurableApplicationContext CONTEXT;

    public static void main(String[] args) {
        // Customize the spring config location
//        System.setProperty("spring.config.additional-location", "file:${user.home}/.halo/,file:${user.home}/halo-dev/");
        additionLocation();

        // Run application
        CONTEXT = SpringApplication.run(Application.class, args);

    }
    public static void additionLocation(){
        ApplicationHome applicationHome=new ApplicationHome(Application.class);
        String parent=applicationHome.getSource().getParent();
        parent=parent+"/conf/application.yaml";
        System.setProperty("spring.config.additional-location", "file:${user.home}/.halo/,file:${user.home}/halo-dev/,file:"+parent);

    }
    /**
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
