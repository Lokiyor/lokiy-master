package com.lokiy.cloud.common.base.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Lokiy
 * @date 2019/7/17 14:36
 * @description: 基本查询参数
 */
@Data
@ApiModel("查询基础参数")
public class BaseQuery implements Serializable {

    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页", required = true, example = "1")
    private Integer pageNum = 1;

    /**
     * 每页条数
     */
    @ApiModelProperty(value = "每页条数", required = true, example = "10")
    private Integer pageSize = 10;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private String orderBy;
}
