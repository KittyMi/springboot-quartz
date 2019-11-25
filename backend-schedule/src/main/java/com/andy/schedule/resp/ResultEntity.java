package com.andy.schedule.resp;

import com.alibaba.fastjson.JSON;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author andy_lai
 * @version 1.0
 * @date 2019/11/23 17:34
 */
@Setter
@Getter
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ResultEntity<T> implements Serializable {

    private static final long serialVersionUID = -587728047893738620L;
    private Boolean flag;
    private int code;
    private T data;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}

