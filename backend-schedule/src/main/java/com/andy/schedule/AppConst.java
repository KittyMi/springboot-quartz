package com.andy.schedule;

/**
 * @author andy_lai
 * @version 1.0
 * @date 2019/11/23 16:43
 */
public final class AppConst {
    public interface JobType {
        String HTTP_JOB = "http_job";
        String JAR_JOB = "jar_job";
        String CUSTOM_JOB = "custom_job";
    }

    public interface JobClass {
        String HTTP_JOB_CLASS_PATH = "com.andy.schedule.job.HttpJob";
        String JAR_JOB_CLASS_PATH = "com.andy.schedule.job.JarJob";
        String CUSTOM_JOB_CLASS_PATH = "com.andy.schedule.job.CustomJob";

    }

    public interface HttpMethod {
        String GET = "get";
        String POST = "post";
    }
}
