package com.andy.schedule.job;

import com.andy.core.util.ElapsedTimeUtil;
import com.andy.schedule.AppConst;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.quartz.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Objects;

/**
 * @author andy_lai
 * @version 1.0
 * @date 2019/11/23 16:55
 * desc Http 调用
 */
/**
 * Created by EalenXie on 2018/6/4 14:29
 * :@DisallowConcurrentExecution : 此标记用在实现Job的类上面,意思是不允许并发执行.
 * :注意org.quartz.threadPool.threadCount线程池中线程的数量至少要多个,否则@DisallowConcurrentExecution不生效
 * :假如Job的设置时间间隔为3秒,但Job执行时间是5秒,设置@DisallowConcurrentExecution以后程序会等任务执行完毕以后再去执行,否则会在3秒时再启用新的线程执行
 */
@DisallowConcurrentExecution
@Slf4j
@Component
public class HttpJob implements Job {

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Resource
    private OkHttpClient okHttpClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey jobKey = context.getJobDetail().getKey();

        String uniqueKey = MessageFormat.format("{0}[{1}]", jobKey.getGroup(), jobKey.getName());
        ElapsedTimeUtil.time(uniqueKey);

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

        // Build Request
        String url = String.valueOf(jobDataMap.get("url"));
        String method = String.valueOf(jobDataMap.get("method"));
        String jsonParams = String.valueOf(jobDataMap.get("jsonParams"));
        Request request = buildRequest(method, url, jsonParams);

        Response response = null;
        String result = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if (Objects.nonNull(response)) {
                result = response.body().string();
            }
        } catch (IOException e) {
            log.error("http调用错误:{}", e.getLocalizedMessage());
            e.printStackTrace();
            throw new JobExecutionException("http调用错误", e);
        } finally {
            log.info("method:{} | url:{} | params:{} | resp:{}", method, url, jsonParams, result);

            if (Objects.nonNull(response)) {
                response.close();
            }
            ElapsedTimeUtil.timeEnd(uniqueKey);
        }

    }

    /**
     * 构建OkHttpClient Request
     * @param method
     * @param url
     * @param jsonParams
     * @return
     */
    private static Request buildRequest(String method, String url, String jsonParams) {
        Request.Builder builder = new Request.Builder();
        Request request = null;
        if (Objects.equals(method, AppConst.HttpMethod.GET)) {
            request = builder.url(url)
                    .get()
                    .build();
        } else {
            RequestBody requestBody = RequestBody.create(JSON, jsonParams);
            request = builder.url(url)
                    .post(requestBody)
                    .build();
        }

        return request;
    }
}
