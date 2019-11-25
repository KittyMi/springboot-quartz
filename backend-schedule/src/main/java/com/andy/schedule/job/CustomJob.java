package com.andy.schedule.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 * @author andy_lai
 * @version 1.0
 * @date 2019/11/23 16:55
 * desc 自定义Job
 */
@Component
@Slf4j
public class CustomJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("自定义任务execute...");
    }
}
