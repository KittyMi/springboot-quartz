package com.andy.schedule.config;

import org.quartz.SchedulerException;
import org.quartz.spi.InstanceIdGenerator;

import java.util.UUID;

/**
 * @author andy_lai
 * @version 1.0
 * @date 2019/11/23 16:49
 * desc: QuartzInstance id生成器
 */
public class QuartzIdGenerator implements InstanceIdGenerator {

    @Override
    public String generateInstanceId() throws SchedulerException {
        try {
            return UUID.randomUUID().toString();
        } catch (Exception e) {
            throw new SchedulerException("Generate UUID Failed!", e);
        }
    }

}