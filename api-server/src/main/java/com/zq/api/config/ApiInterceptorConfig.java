package com.zq.api.config;

import com.zq.api.interceptor.ApiInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApiInterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApiInterceptor()).addPathPatterns("/api/**");
//                .excludePathPatterns("/static/**")
//                .excludePathPatterns("/login")
//                .excludePathPatterns("/logout")
//                .excludePathPatterns("/getImage")
//                .excludePathPatterns("/do_login")
//                .excludePathPatterns("/index");
    }
}
