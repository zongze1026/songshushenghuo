package com.yitkeji.songshushenghuo.component;


import com.yitkeji.songshushenghuo.Filter.AdminInterfaceFilter;
import com.yitkeji.songshushenghuo.Filter.AgentInterfaceFilter;
import com.yitkeji.songshushenghuo.Filter.AppInterfaceFilter;
import com.yitkeji.songshushenghuo.Filter.MainInterfaceFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class FilterConfig {


    @Autowired
    private MainInterfaceFilter mainInterfaceFilter;

    @Autowired
    private AppInterfaceFilter appInterfaceFilter;

    @Autowired
    private AdminInterfaceFilter adminInterfaceFilter;

    @Autowired
    private AgentInterfaceFilter agentInterfaceFilter;

    @Bean
    @Order
    public FilterRegistrationBean mainFilterRegist(){
        FilterRegistrationBean frBean = new FilterRegistrationBean();
        frBean.setFilter(mainInterfaceFilter);
        frBean.addUrlPatterns("/*");
        return frBean;
    }


    @Bean
    @Order(Integer.MAX_VALUE - 1)
    public FilterRegistrationBean appFilterRegist(){
        FilterRegistrationBean frBean = new FilterRegistrationBean();
        frBean.setFilter(appInterfaceFilter);
        frBean.addUrlPatterns("/app/*");
        return frBean;
    }


    @Bean
    @Order(Integer.MAX_VALUE - 2)
    public FilterRegistrationBean adminFilterRegist(){
        FilterRegistrationBean frBean = new FilterRegistrationBean();
        frBean.setFilter(adminInterfaceFilter);
        frBean.addUrlPatterns("/admin/*");
        return frBean;
    }


    @Bean
    @Order(Integer.MAX_VALUE - 3)
    public FilterRegistrationBean agentFilterRegist(){
        FilterRegistrationBean frBean = new FilterRegistrationBean();
        frBean.setFilter(agentInterfaceFilter);
        frBean.addUrlPatterns("/agent/*");
        return frBean;
    }


}
