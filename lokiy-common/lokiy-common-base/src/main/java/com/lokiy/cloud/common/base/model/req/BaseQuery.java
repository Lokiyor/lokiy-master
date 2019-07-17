package com.lokiy.cloud.common.base.model.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Lokiy
 * @date 2019/7/17 14:36
 * @description: 基本查询参数
 */
@Data
public class BaseQuery implements Serializable {

    /**
     * 当前也
     */
    private Integer pageNum = 1;

    /**
     * 每页条数
     */
    private Integer pageSize = 10;

    /**
     * 排序
     */
    private String orderBy;
}
