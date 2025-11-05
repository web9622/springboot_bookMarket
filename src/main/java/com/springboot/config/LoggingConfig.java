package com.springboot.config;

import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.springboot.interceptor.AuditingInterceptor;
import com.springboot.interceptor.MonitoringInterceptor;


@Configuration
public class LoggingConfig implements WebMvcConfigurer {
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MonitoringInterceptor());     
       registry.addInterceptor(new AuditingInterceptor());   
     //   .excludePathPatterns("/BookMarket/css/**", "/BookMarket/images/**", "/js/**");
      
    }

}