package com.andy.schedule.bean;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.quartz.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author andy_lai
 * @date 2018/6/23 14:13
 */
@ApiModel(value = "JobDetailBO", description = "Quartz JobDetail等价类")
@ToString
public class JobDetailBO {

    @Getter
    @Setter
    private JobBO jobBO;
    @Getter
    @Setter
    private Set<TriggerBO> triggerBOSet;

    /**
     * 处理JobDetail
     */
    public transient Consumer<JobDetail> fillWithQuartzJobDetail = jobDetail -> {
        //Job
        JobKey jobKey = jobDetail.getKey();

        jobBO = new JobBO()
                .setName(jobKey.getName())
                .setGroup(jobKey.getGroup())
                .setDescription(jobDetail.getDescription())
                .setTargetClass(jobDetail.getJobClass().getCanonicalName());
        //ext
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        if (Objects.nonNull(jobDataMap)) {
            jobBO.setExtInfo(jobDataMap.getWrappedMap());
        }
    };

    /**
     * 处理triggers
     */
    public transient Consumer<List<Trigger>> fillWithQuartzTriggers = triggerList -> {
        // Triggers
        Set<TriggerBO> tbSet = triggerList.stream().map(trigger -> {

            TriggerKey triggerKey = trigger.getKey();
            TriggerBO triggerBO = new TriggerBO()
                    .setName(triggerKey.getName())
                    .setGroup(triggerKey.getGroup())
                    .setDescription(trigger.getDescription());

            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                triggerBO.setCronExpression(cronTrigger.getCronExpression());
            }

            return  triggerBO;
        }).collect(Collectors.toSet());

        triggerBOSet = tbSet;
    };
}
