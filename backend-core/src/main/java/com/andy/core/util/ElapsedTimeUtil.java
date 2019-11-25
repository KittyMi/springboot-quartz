package com.andy.core.util;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author andy_lai
 * @version 1.0
 * @date 2019/11/19 15:54
 */
@Slf4j
public class ElapsedTimeUtil {

    /**
     * 每个线程持有一个timeHolder
     * 保证time timeEnd 方法成对出现
     */
    private static final ThreadLocal<TimeHolder> THREAD_TIME_HOLDER = new ThreadLocal<>();

    private static class TimeHolder {
        private Map<String,Long> keyElapsedTime;
        public TimeHolder(){
            keyElapsedTime = new HashMap<>();
        }

        public boolean canRemoved(){
            return Objects.isNull(keyElapsedTime) || keyElapsedTime.isEmpty();
        }

        public void start(String key){

            // 同一线程内 相同key 直接覆盖
            keyElapsedTime.put(key,System.nanoTime());
        }

        public long elapse(String key){
            long endTime = System.nanoTime();
            long startTime = keyElapsedTime.getOrDefault(key,System.nanoTime());
            return endTime - startTime;
        }
    }

    public static void time(String key){
        TimeHolder timeHolder = THREAD_TIME_HOLDER.get();
        if (Objects.isNull(timeHolder)) {
            timeHolder = new TimeHolder();
            THREAD_TIME_HOLDER.set(timeHolder);
        }
        timeHolder.start(key);
    }

    public static void timeEnd(String key){
        TimeHolder timeHolder = THREAD_TIME_HOLDER.get();
        long time = timeHolder.elapse(key);
        if (timeHolder.canRemoved()) {
            THREAD_TIME_HOLDER.remove();
        }
        long threadId = Thread.currentThread().getId();
        log.info("thread: {} | key: {} | {}ms",threadId, key, TimeUnit.NANOSECONDS.toMillis(time));
    }
}
