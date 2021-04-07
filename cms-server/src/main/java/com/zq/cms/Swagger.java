package com.zq.cms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author bici
 */
@Configuration
@EnableSwagger2
public class Swagger {

    @Value("${spring.cloud.config.profile}")
    private String profile;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("后台管理")
                .enable(!"product".equals(profile)) //生产环境关闭
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zq.cms.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("后台接口文档")
                .description("查看接口文档")
                .build();
    }

    @Bean
    public Docket app() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("App项目")
                .enable(!"product".equals(profile)) //生产环境关闭
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zq.cms.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(appInfo());
    }

    private ApiInfo appInfo() {
        return new ApiInfoBuilder()
                .title("APP接口文档")
                .description("查看接口文档")
                .build();
    }

}