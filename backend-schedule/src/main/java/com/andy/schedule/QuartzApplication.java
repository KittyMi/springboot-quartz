package com.andy.schedule;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.spring4all.swagger.EnableSwagger2Doc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author andy_lai
 * @version 1.0
 * @date 2019/11/23 16:44
 */
@EnableTransactionManagement
@SpringBootApplication(
        exclude = DruidDataSourceAutoConfigure.class
)
@ComponentScan(
        basePackages = {
                "com.andy.schedule",
                "com.andy.core"
        }
)
@EnableSwagger2Doc
@Slf4j
public class QuartzApplication {
    public static void main(String[] args) {
        log.info("调度服务启动中....");
        SpringApplication.run(QuartzApplication.class, args);
        log.info("调度服务启动成功");
    }
}
