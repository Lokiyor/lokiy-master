package com.lokiy.cloud.common.base.model.other;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Lokiy
 * @date 2019/7/17 14:47
 * @description: 组织树结构
 */
@Data
@ApiModel("组织树基础对象")
public class BaseTree<ID, E> implements Serializable {

    /**
     * 本节点id
     */
    @ApiModelProperty("本节点id")
    private ID id;

    /**
     * 父节点id
     */
    @ApiModelProperty("父节点id")
    private ID pid;

    /**
     * 是否有子节点
     */
    @ApiModelProperty("是否包含子节点")
    private boolean hasChild = false;

    /**
     * 子节点集合
     */
    @ApiModelProperty("子节点集合")
    private List<E> children;
}
