package com.andy.core.config;

import com.andy.core.exception.GlobalExceptionResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.List;

/**
 * @author andy_lai
 * @version 1.0
 * @date 2019/11/19 15:53
 */
@Configuration
@ConditionalOnMissingBean(name = "webMvcConfig")
public class DefaultWebMvcConfig implements BaseWebMvcConfig{

    /**
     * 配置全局异常处理器
     * @param resolvers
     */
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        BaseWebMvcConfig.super.configureHandlerExceptionResolvers(resolvers);
        resolvers.add(new GlobalExceptionResolver());
    }

}
