package com.andy.schedule.service.impl;

import com.andy.schedule.bean.JobDetailBO;
import com.andy.schedule.service.IQuartzJobDetailService;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author andy_lai
 * @date 2018/6/23 15:00
 */
@Service
@Transactional(rollbackFor = {
        Exception.class
})
public class QuartzJobDetailServiceImpl implements IQuartzJobDetailService {

    @Resource
    private Scheduler scheduler;

    /**
     * 任务列表
     *
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<JobDetailBO> listAllJobDetail() {
        List<JobDetailBO> list = new ArrayList<>();

        // 数据处理
        try {
            Set<JobKey> jobKeySet = scheduler.getJobKeys(GroupMatcher.anyJobGroup());

            list = jobKeySet.stream().map(jobKey -> {
                List<Trigger> triggerList = getTriggerByKey(jobKey);
                JobDetail jobDetail = getJobDetailByKey(jobKey);

                //JobDetailBO
                JobDetailBO jobDetailBO = new JobDetailBO();
                jobDetailBO.fillWithQuartzJobDetail.accept(jobDetail);
                jobDetailBO.fillWithQuartzTriggers.accept(triggerList);
                return jobDetailBO;
            }).collect(Collectors.toList());

        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 根据JobKey列出任务
     *
     * @param jobKey
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public JobDetailBO listJobDetail(JobKey jobKey) {
        JobDetailBO jobDetailBO = new JobDetailBO();

        JobDetail jobDetail = getJobDetailByKey(jobKey);
        if (Objects.nonNull(jobDetail)) {
            List<Trigger> triggerList = getTriggerByKey(jobKey);
            jobDetailBO.fillWithQuartzJobDetail.accept(jobDetail);
            jobDetailBO.fillWithQuartzTriggers.accept(triggerList);
        }
        return jobDetailBO;
    }

    /**
     * 新增
     *
     * @param jobDetailBO
     * @return
     */
    @Override
    public boolean add(JobDetailBO jobDetailBO) {
        JobDetail jobDetail = jobDetailBO.getJobBO().convert2QuartzJobDetail();
        Set<CronTrigger> triggerSet = jobDetailBO.getTriggerBOSet().stream().map(
                triggerBO -> triggerBO.convert2QuartzTrigger(jobDetail)
        ).collect(Collectors.toSet());

        // 如果存在则替换
        try {
            scheduler.scheduleJob(jobDetail, triggerSet, true);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据JobKey删除
     *
     * @param list
     * @return
     */
    @Override
    public boolean remove(List<JobKey> list) {
        try {
            return scheduler.deleteJobs(list);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 暂停
     *
     * @param groupMatcher
     * @return
     */
    @Override
    public boolean pause(GroupMatcher<JobKey> groupMatcher) {
        try {
            scheduler.pauseJobs(groupMatcher);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 暂停所有
     *
     * @return
     */
    @Override
    public boolean pauseAll() {
        try {
            scheduler.pauseAll();
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 恢复
     *
     * @param groupMatcher
     * @return
     */
    @Override
    public boolean resume(GroupMatcher<JobKey> groupMatcher) {
        try {
            scheduler.resumeJobs(groupMatcher);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 恢复所有
     *
     * @return
     */
    @Override
    public boolean resumeAll() {
        try {
            scheduler.resumeAll();
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 立即触发任务
     *
     * @param jobKey
     * @param jobDataMap
     * @return
     */
    @Override
    public boolean triggerNow(JobKey jobKey, JobDataMap jobDataMap) {
        try {
            scheduler.triggerJob(jobKey, jobDataMap);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 查询JobDetail
     *
     * @param jobKey
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public JobDetail getJobDetailByKey(JobKey jobKey) {
        JobDetail jobDetail = null;
        try {
            jobDetail = scheduler.getJobDetail(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return jobDetail;
    }

    /**
     * 查询Trigger
     *
     * @param jobKey
     * @return
     */
    @Override
    public List<Trigger> getTriggerByKey(JobKey jobKey) {
        List<Trigger> list = new ArrayList<>();
        try {
            list = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return list;
    }
}
