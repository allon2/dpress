package cn.ymotel.dpress;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.OracleTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.IOException;

/**
 * Mybatis Plus代码生成器
 * @author rdc
 */
public class MybatisGenerator {

    private static final String OUTPUT_DIR = System.getProperty("user.home");

    private static final String PACKAGE_BASE_NAME = "cn.ymotel.dpress.entity";

    private static final String PACKAGE_MODULE_NAME = "";

    private static final String PACKAGE_ENTITY_NAME = "model";
    private static final String PACKAGE_MAPPER_NAME = "mapper";
    private static final String PACKAGE_XML_NAME = "sql-map";

    public static void main(String[] args) throws IOException {
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setDateType(DateType.ONLY_DATE);
        gc.setOutputDir(OUTPUT_DIR);
        gc.setFileOverride(true);
        // 开启 activeRecord 模式
        gc.setActiveRecord(true);
        // XML 二级缓存
        gc.setEnableCache(false);
        // XML ResultMap
        gc.setBaseResultMap(false);
        // XML columList
        gc.setBaseColumnList(false);
        gc.setAuthor("dpress");


        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        // gc.setMapperName("%sDao");
        // gc.setXmlName("%sDao");
        // gc.setServiceName("MP%sService");
        // gc.setServiceImplName("%sServiceDiy");
        // gc.setControllerName("%sAction");

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("111111");
        dsc.setUrl("jdbc:mysql://127.0.0.1:3306/dpress1?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true");
//        dsc.setTypeConvert(new OracleTypeConvert() {
//            // 自定义数据库表字段类型转换【可选】
//            @Override
//            public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
//                String t = fieldType.toUpperCase();
//
//                if (t.contains("NUMBER")) {
//                    if (t.matches("NUMBER")) {
//                        return DbColumnType.INTEGER;
//                    }
//                    if (t.matches("NUMBER\\(\\d\\)")) {
//                        return DbColumnType.INTEGER;
//                    }
//                    if (t.matches("NUMBER\\(\\d{2,}\\)")) {
//                        return DbColumnType.LONG;
//                    }
//                    if (t.matches("NUMBER\\(\\d+,\\d+\\)")) {
//                        return DbColumnType.BIG_DECIMAL;
//                    }
//
//                    return DbColumnType.DOUBLE;
//                }
//
//                // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
//                return super.processTypeConvert(gc, fieldType);
//            }
//        });

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // 字段名生成策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // 全局大写命名 ORACLE 注意
        // strategy.setCapitalMode(true);
        // 此处可以修改为您的表前缀
        // strategy.setTablePrefix(new String[] { "bmd_", "mp_" });
        // 需要生成的表
//        strategy.setInclude(new String[]{"POSTS"});
        // 排除生成的表
        // strategy.setExclude(new String[]{"test"});

        // 自定义实体父类
        // strategy.setSuperEntityClass("com.csii.payment.model.CommonEntity");
        // 自定义实体，公共字段
        // strategy.setSuperEntityColumns(new String[]{"GMT_CREATE_DATETIME", "GMT_MODIFY_DATETIME"});
        // 自定义 mapper 父类
        // strategy.setSuperMapperClass("com.csii.demo.TestMapper");
        // 自定义 service 父类
        // strategy.setSuperServiceClass("com.csii.demo.TestService");
        // 自定义 service 实现类父类
        // strategy.setSuperServiceImplClass("com.csii.demo.TestServiceImpl");
        // 自定义 controller 父类
        // strategy.setSuperControllerClass("com.csii.demo.TestController");

        // 自定义需要填充的字段
        // List<TableFill> tableFillList = new ArrayList<>();
        // tableFillList.add(new TableFill("ASDD_SS", FieldFill.INSERT_UPDATE));
        // strategy.setTableFillList(tableFillList);

        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        // strategy.setEntityColumnConstant(true);

        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
        strategy.setEntityBuilderModel(true);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(PACKAGE_BASE_NAME);
        pc.setModuleName(PACKAGE_MODULE_NAME);
        pc.setEntity(PACKAGE_ENTITY_NAME);
        pc.setMapper(PACKAGE_MAPPER_NAME);
        pc.setXml(PACKAGE_XML_NAME);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        TemplateConfig tc = new TemplateConfig();
        tc.setController(null);
        tc.setService(null);
        tc.setServiceImpl(null);

        mpg.setCfg(cfg);
        mpg.setGlobalConfig(gc);
        mpg.setDataSource(dsc);
        mpg.setStrategy(strategy);
        mpg.setPackageInfo(pc);
        mpg.setTemplate(tc);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        // 执行生成
        mpg.execute();
    }
}
