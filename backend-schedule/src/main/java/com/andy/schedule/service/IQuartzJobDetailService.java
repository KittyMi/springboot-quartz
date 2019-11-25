package com.andy.schedule.service;

import com.andy.schedule.bean.JobDetailBO;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;

import java.util.List;

/**
 * @author andy_lai
 * @date 2018/6/22 20:14
 */
public interface IQuartzJobDetailService {

    /**
     * 任务列表
     * @return
     */
    List<JobDetailBO> listAllJobDetail();

    /**
     * 根据JobKey列出任务
     * @param jobKey
     * @return
     */
    JobDetailBO listJobDetail(JobKey jobKey);

    /**
     * 新增
     * @param jobDetailBO
     * @return
     */
    boolean add(JobDetailBO jobDetailBO);

    /**
     * 根据JobKey删除
     * @param list
     * @return
     */
    boolean remove(List<JobKey> list);

    /**
     * 暂停
     * @param groupMatcher
     * @return
     */
    boolean pause(GroupMatcher<JobKey> groupMatcher);

    /**
     * 暂停所有
     * @return
     */
    boolean pauseAll();

    /**
     * 恢复
     * @param groupMatcher
     * @return
     */
    boolean resume(GroupMatcher<JobKey> groupMatcher);

    /**
     * 恢复所有
     * @return
     */
    boolean resumeAll();

    /**
     * 立即触发任务
     * @param jobKey
     * @param jobDataMap
     * @return
     */
    boolean triggerNow(JobKey jobKey, JobDataMap jobDataMap);

    /**
     * 查询JobDetail
     * @param jobKey
     * @return
     */
    JobDetail getJobDetailByKey(JobKey jobKey);

    /**
     * 查询Trigger
     * @param jobKey
     * @return
     */
    List<Trigger> getTriggerByKey(JobKey jobKey);
}
