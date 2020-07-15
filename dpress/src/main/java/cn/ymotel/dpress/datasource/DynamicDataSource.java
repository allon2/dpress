package cn.ymotel.dpress.datasource;

import cn.ymotel.dpress.Utils;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class DynamicDataSource extends MockDataSource {
//    @Autowired
    private DruidProperties druidProperties;

    private Map<String,DataSource> dataSourceMap = new HashMap();

    public DynamicDataSource(DruidProperties druidProperties) {
        super();
        this.druidProperties=druidProperties;
    }

//    @Bean
//    public DataSource DeleteDataSource(DruidProperties druidProperties){
//        return new DynamicDataSource(druidProperties);
//    }
    @Override
    public DataSource getDataSource(){
        DataSource dataSource=  dataSourceMap.get("DB");
        if(dataSource!=null){
            return dataSource;
        }else{
            if(Utils.isInstall()){
                dataSource= buildDataSource();
                dataSourceMap.put("DB",dataSource);
                return dataSource;
            }
        }
        dataSource=dataSourceMap.get("MOCK");
        if(dataSource==null){
            dataSource=buildMockDataSource();
            dataSourceMap.put("MOCK",dataSource);
        }
        return dataSource;

    }
    private void setH2Property(DruidDataSource dataSource){
        dataSource.setUrl("jdbc:h2:mem:test_db");
        dataSource.setUsername("sa");
        dataSource.setPassword("123456");
        dataSource.setDriverClassName("org.h2.Driver");
    }
    private DataSource buildMockDataSource(){
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();

        setH2Property(dataSource);
        return druidProperties.druidDataSource(dataSource);
    }
     private DruidDataSource buildDataSource(){
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
         try {
             setMysqlProperties(dataSource);
         } catch (IOException e) {
             e.printStackTrace();
         }
         return druidProperties.druidDataSource(dataSource);
    }

    private void setMysqlProperties(DruidDataSource dataSource) throws IOException {
        File file=Utils.getJdbcFile();
        Properties ps=new Properties();
        ps.load(new FileInputStream(file));
        dataSource.setUrl(ps.getProperty("url"));
        dataSource.setUsername(ps.getProperty("username"));
        dataSource.setPassword(ps.getProperty("password"));
        if(ps.containsKey("maxActive")){
            dataSource.setMaxActive(Integer.parseInt(ps.getProperty("maxActive")));
        }
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

    }
}
