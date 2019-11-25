package com.andy.core.config;

import com.andy.core.interceptor.LogInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author andy_lai
 * @date 2019-11-19
 * @desc 默认的WebMvcConfigurer
 */
public interface BaseWebMvcConfig extends WebMvcConfigurer {
    /**
     * 日志拦截器  打印日志
     * @param registry
     */
    @Override
    default void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor());
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
