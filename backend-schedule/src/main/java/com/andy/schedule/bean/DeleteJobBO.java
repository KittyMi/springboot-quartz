package com.andy.schedule.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
 * @author andy_lai
 * @version 1.0
 * @date 2019/11/25 10:06
 */
@Slf4j
@ToString
@Setter
@Getter
@Accessors(chain = true)
@ApiModel(description = "Job停止删除业务实体")
public class DeleteJobBO implements Serializable {

    @ApiModelProperty(value = "Job组", example = "HTTP_JOB")
    private String group;
    @ApiModelProperty(value = "Job名称", example = "[\"test_job\"]")
    private List<String> nameList;
}
