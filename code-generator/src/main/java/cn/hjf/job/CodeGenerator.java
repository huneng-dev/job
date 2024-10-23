package cn.hjf.job;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.sql.Types;
import java.util.Collections;

public class CodeGenerator {
    public static void main(String[] args) {
        // 数据库url
        String url = "jdbc:mysql://localhost:3306/job_resume?characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8&rewriteBatchedStatements=true&allowPublicKeyRetrieval=true";
        String username = "root";
        String password = "927733";

        // 需要生成代码的表名
//        String[] tableNames = {"benefit_type","company_address","company_album","company_benefit","company_business_license","company_department","company_industry","company_info","company_size","legal_person_info"};
        String[] tableNames = {
                "certification",
                "education_background",
                "honor_award",
                "job_expectation",
                "project_experience",
                "resume_favorite",
                "resume_info",
                "work_experience"
        };
        // 指定输出目录
        String outputDir = "D:/Java/job/code-generator/src/main/java";

        // 基础包名 basePackageName + moduleName = cn.hjf.job.candidate
        String basePackageName = "cn.hjf.job.";

        // 模块名 basePackageName + moduleName = cn.hjf.job.candidate
        String moduleName = "resume";

        // mapperXml 文件输出目录
        String mapperXmlOutputPath = "D:/Java/job/code-generator/src/main/resources/mapper/" + moduleName;


        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("hjf") // 设置作者
//                            .enableSwagger() // 开启 swagger 模式
                            .disableOpenDir()
//                            .dateType(DateType.ONLY_DATE)
                            .outputDir(outputDir); // 指定输出目录
                })
                .dataSourceConfig(builder ->
                        builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                            int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                            if (typeCode == Types.TINYINT) {
                                return DbColumnType.INTEGER;
                            } else if (typeCode == Types.TIME) {
                                return DbColumnType.LOCAL_TIME;
                            }
                            return typeRegistry.getColumnType(metaInfo);
                        })
                )
                .packageConfig(builder -> {
                    builder.entity("entity")// 实体类包名
                            .parent(basePackageName + moduleName)// 父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
//                            .controller("controller")// 控制层包名
                            .mapper("mapper")// mapper层包名
                            .service("service")// service层包名
                            .serviceImpl("service.impl")// service实现类包名
                            // 自定义mapper.xml文件输出目录
                            .pathInfo(Collections.singletonMap(OutputFile.xml,
                                    mapperXmlOutputPath));
                })
                .strategyConfig(builder -> {
                    // 设置要生成的表名
                    builder.addInclude(tableNames)
                            //.addTablePrefix("sys_")//设置表前缀过滤
                            .controllerBuilder()
                            /**
                             * 实体配置
                             */
                            .entityBuilder()
                            // .superClass(SuperCommomPO.class) // 设置实体类父类-父类中存在的字段不会在实体类中存在
//                            .enableLombok()
                            .naming(NamingStrategy.underline_to_camel)// 数据表映射实体命名策略：默认下划线转驼峰underline_to_camel
                            .columnNaming(NamingStrategy.underline_to_camel)// 表字段映射实体属性命名规则：默认null，不指定按照naming执行
                            // 忽然字段
                            .addIgnoreColumns("id", "create_time", "update_time", "is_deleted")
                            .formatFileName("%s")// 格式化实体名称，%s取消首字母I,
                            /**
                             * mapper配置
                             */
                            .mapperBuilder()
                            .enableMapperAnnotation()// 开启mapper注解
                            .enableBaseResultMap()// 启用xml文件中的BaseResultMap 生成
                            .enableBaseColumnList()// 启用xml文件中的BaseColumnList
                            .formatMapperFileName("%sMapper")// 格式化Dao类名称
                            .formatXmlFileName("%sMapper")// 格式化xml文件名称
                            /**
                             * service配置
                             */
                            .serviceBuilder()
                            .formatServiceFileName("%sService")// 格式化 service 接口文件名称
                            .formatServiceImplFileName("%sServiceImpl")// 格式化 service 接口文件名称
                            .controllerBuilder()
                            .enableRestStyle();
                })
                .templateConfig(builder -> {
                    builder.entity("/templates/entity.java");
                    builder.service("/templates/service.java");
                    builder.serviceImpl("/templates/serviceImpl.java");
                    builder.mapper("/templates/mapper.java");
                    builder.xml("/templates/mapper.xml");
                })
                .templateEngine(new VelocityTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

}
