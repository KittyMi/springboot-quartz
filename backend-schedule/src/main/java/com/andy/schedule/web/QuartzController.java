package com.andy.schedule.web;


import com.andy.schedule.bean.DeleteJobBO;
import com.andy.schedule.bean.JobDetailBO;
import com.andy.schedule.bean.TriggerJobBO;
import com.andy.schedule.resp.ResultEntity;
import com.andy.schedule.resp.ResultFactory;
import com.andy.schedule.service.IQuartzJobDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.quartz.JobKey;
import org.quartz.core.jmx.JobDataMapSupport;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ly
 * @date 2018/6/23 16:32
 */
@RestController
@RequestMapping("job")
@Api(tags = "调度服务模块")
public class QuartzController {

    @Resource
    private IQuartzJobDetailService quartzJobDetailService;

    @ApiOperation(value = "获取任务列表")
    @GetMapping(value = "/list")
    public ResultEntity list() {
        return ResultFactory.success(quartzJobDetailService.listAllJobDetail());
    }

    @ApiOperation(value = "查询指定JobKey的JobDetail")
    @ApiImplicitParams( value = {
            @ApiImplicitParam(name = "group", value = "Job组", required = true, paramType = "query", example = "HTTP_JOB"),
            @ApiImplicitParam(name = "name", value = "Job名称", required = true, paramType = "query", example = "test_job")
    } )
    @GetMapping("/list/{group}/{name}")
    public ResultEntity listByJobKey(
            @PathVariable String group,
            @PathVariable String name
    ){
        JobKey jobKey = new JobKey(name, group);
        return ResultFactory.success(quartzJobDetailService.listJobDetail(jobKey));
    }

    @ApiOperation(value = "添加任务Job")
    @PostMapping("/add")
    public ResultEntity add(@RequestBody JobDetailBO jobDetailBO) {
        return ResultFactory.success(quartzJobDetailService.add(jobDetailBO));
    }

    @ApiOperation(value = "批量删除Job")
    @DeleteMapping("/delete")
    public ResultEntity delete(@RequestBody List<DeleteJobBO> jobKeyGroups) {
        List<JobKey> jobKeys = new ArrayList<>();
        jobKeyGroups.forEach(bean ->
                bean.getNameList().forEach(name -> {
                    JobKey jobKey = new JobKey(name, bean.getGroup());
                    jobKeys.add(jobKey);
                })
        );
        return ResultFactory.success(quartzJobDetailService.remove(jobKeys));
    }

    @ApiOperation(value = "立即触发任务")
    @PostMapping(value = "/trigger")
    public ResultEntity triggerNow(
            @RequestBody TriggerJobBO triggerJobBO
    ) {
        JobKey jobKey = new JobKey(triggerJobBO.getName(), triggerJobBO.getGroup());
        return ResultFactory.success(
                quartzJobDetailService.triggerNow(jobKey, JobDataMapSupport.newJobDataMap(triggerJobBO.getJobData()))
        );
    }
}
