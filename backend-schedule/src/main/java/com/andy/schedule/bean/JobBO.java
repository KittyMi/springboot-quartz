package com.andy.schedule.bean;

import com.andy.schedule.AppConst;
import com.andy.schedule.job.CustomJob;
import com.andy.schedule.job.HttpJob;
import com.andy.schedule.job.JarJob;
import com.google.common.base.Preconditions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.core.jmx.JobDataMapSupport;
import org.springframework.util.ClassUtils;

import java.util.*;

/**
 * @author andy_lai
 * @date 2018/6/22 20:34
 * Job的业务实体
 */
@Slf4j
@ToString
@Setter
@Getter
@Accessors(chain = true)
@ApiModel(description = "Job业务实体")
public class JobBO {
    private static final Map<String, Class<? extends Job>> SUPPORTED_JOB_TYPES =
            new HashMap<String, Class<? extends Job>>(){
                {
                    put(AppConst.JobType.HTTP_JOB, HttpJob.class);
                    put(AppConst.JobType.JAR_JOB, JarJob.class);
                    put(AppConst.JobType.CUSTOM_JOB, CustomJob.class);
                }
            };

    /**
     * 可供支持的扩展字段
     * type: AppConst.JobType
     * method: AppConst.HttpMethod
     * url: Http invoke url
     * jsonParams: json params
     */
    private static final Set<String> SUPPORTED_EXT_FIELDS = new HashSet<String>() {
        {
            //http
            add("type");
            add("method");
            add("url");
            add("jsonParams");
            //jar
            add("vmParam");
            add("parameter");
            add("jarPath");

        }
    };

    @ApiModelProperty(value = "Job名称", example = "test_job")
    private String name;
    @ApiModelProperty(value = "Job组", example = "HTTP_JOB")
    private String group;
    @ApiModelProperty(value = "HttpJob, JarJob", example = "com.andy.schedule.job.HttpJob")
    private String targetClass;
    @ApiModelProperty(value = "Job的描述", example = "定时请求百度")
    private String description;

    @ApiModelProperty(
            value = "拓展字段",
            example = "{method: 'get', jsonParams: '', type: 'http_job', url: 'http://www.baidu.com'}"
    )
    private Map<String, Object> extInfo;


    /**
     * 转换为Quartz JobDetail
     * @return
     */
    public JobDetail convert2QuartzJobDetail() {
        Class<? extends Job> clazz = SUPPORTED_JOB_TYPES.get(this.extInfo.get("type"));
        Preconditions.checkNotNull(clazz, "未找到匹配type的Job");

        if (Objects.isNull(this.targetClass)) {
            String type = String.valueOf(this.extInfo.get("type"));
            clazz = SUPPORTED_JOB_TYPES.get(type);
            Preconditions.checkNotNull(clazz, "未找到匹配type的Job");
            targetClass = clazz.getCanonicalName();
        }

        try {
            clazz = (Class<? extends Job>) ClassUtils.resolveClassName(targetClass, getClass().getClassLoader());
        } catch (IllegalArgumentException e) {
            log.error("加载类错误:", e.getLocalizedMessage());
        }

        return JobBuilder.newJob()
                .ofType(clazz)
                .withIdentity(name, group)
                .withDescription(description)
                .setJobData(JobDataMapSupport.newJobDataMap(extInfo))
                .build();
    }
}
