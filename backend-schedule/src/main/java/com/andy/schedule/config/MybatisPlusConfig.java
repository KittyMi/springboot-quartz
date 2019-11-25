package com.andy.schedule.config;


import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author ly
 * @date 2019/5/20 17:06
 * description: Mybatis Plus Config
 */
@EnableTransactionManagement
@Configuration
@MapperScan(basePackages = "com.andy.schedule.mapper")
public class MybatisPlusConfig {

    /**
     * @return 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 性能分析插件仅在开发环境中使用
     * @return 性能分析插件
     */
    @Bean
    @Profile({"dev", "default"})
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }
}
