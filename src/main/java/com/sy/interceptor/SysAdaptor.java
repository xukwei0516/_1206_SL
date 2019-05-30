package com.sy.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SysAdaptor implements WebMvcConfigurer {

    @Autowired
    private SysInterceptor sysInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        InterceptorRegistration interceptorRegistration = registry.addInterceptor(sysInterceptor);
        interceptorRegistration.excludePathPatterns("/login.html","/statics/**","/main.html","/");
        interceptorRegistration.addPathPatterns("/**");

    }
}
