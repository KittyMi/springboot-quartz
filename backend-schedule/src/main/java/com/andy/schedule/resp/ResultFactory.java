package com.andy.schedule.resp;

import lombok.NonNull;

import static com.andy.core.Constant.EMPTY_OBJECT;

/**
 * @author andy_lai
 * @version 1.0
 * @date 2019/11/23 17:34
 */
public class ResultFactory {
    /**
     * 成功（无返回值）
     * @return  ResultEntity<T>
     */
    @NonNull
    public static ResultEntity success() {
        return success(EMPTY_OBJECT);
    }

    /**
     * 成功（带返回值）
     * @param data 数据
     * @param <T>  类型
     * @return  ResultEntity<T>
     */
    @NonNull
    public static <T> ResultEntity<T> success(@NonNull T data) {
        return new ResultEntity<T>()
                .setFlag(true)
                .setCode(0)
                .setData(data);
    }






}
