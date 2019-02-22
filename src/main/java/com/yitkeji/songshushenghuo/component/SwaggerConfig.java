package com.yitkeji.songshushenghuo.component;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket appApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("app")
                .select()
                .paths(regex("/(app)/.*"))
                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(new ApiInfo("移动端API", null, "1.0", "", new Contact("", "", ""), "", "", new ArrayList()));
    }

    @Bean
    public Docket pcApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("pc")
                .select()
                .paths(regex("/system/.*"))
                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(new ApiInfo("PC端API", null, "1.0", "", new Contact("", "", ""), "", "", new ArrayList()));
    }
    @Bean
    public Docket adminApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("admin")
                .select()
                .paths(regex("/admin/.*"))
                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(new ApiInfo("管理后台API", null, "1.0", "", new Contact("", "", ""), "", "", new ArrayList()));
    }
    @Bean
    public Docket agentApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("agent")
                .select()
                .paths(regex("/agent/.*"))
                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(new ApiInfo("代理管理后台API", null, "1.0", "", new Contact("", "", ""), "", "", new ArrayList()));
    }
}