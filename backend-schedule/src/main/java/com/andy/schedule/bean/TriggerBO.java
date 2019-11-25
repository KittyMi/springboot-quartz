package com.andy.schedule.bean;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.quartz.*;

import java.text.ParseException;

/**
 * @author andy_lai
 * @date 2018/6/23 13:21
 * 触发器的业务实体
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class TriggerBO {

    @ApiModelProperty(value = "Trigger名称", example = "test_trigger")
    private String name;
    @ApiModelProperty(value = "Trigger组", example = "HTTP_TRIGGER")
    private String group;
    @ApiModelProperty(value = "cron表达式", example = "0/30 * * * * ?")
    private String cronExpression;
    @ApiModelProperty(value = "Trigger描述", example = "每30秒执行一次")
    private String description;

    /**
     * 转化为Quartz Trigger
     * @param jobDetail
     * @return
     */
    public CronTrigger convert2QuartzTrigger(JobDetail jobDetail) {
        CronExpression ce = null;

        try {
            Preconditions.checkArgument(!Strings.isNullOrEmpty(cronExpression),"cronExpression参数非法");
            ce = new CronExpression(cronExpression);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withSchedule(CronScheduleBuilder.cronSchedule(ce))
                .withIdentity(name, group)
                .withDescription(description)
                .build();
    }
}
