package com.finger.limit.config;

import com.finger.limit.interceptor.RequestLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
 

/**
 * create by: lixh
 * description: TODO
 * create time: 2021/8/4 9:36
 * @return
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private RequestLimitInterceptor requestLimitInterceptor;
 
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLimitInterceptor)
 
                //拦截所有请求路径
                .addPathPatterns("/**")
                //再设置 放开哪些路径
                .excludePathPatterns("/static/**","/auth/login");
    }
 
}