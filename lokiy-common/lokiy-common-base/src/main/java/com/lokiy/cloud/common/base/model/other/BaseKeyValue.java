package com.lokiy.cloud.common.base.model.other;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Lokiy
 * @date 2019/7/17 14:53
 * @description: 键值对对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("通用键值对象")
public class BaseKeyValue<K, V> implements Serializable {

    /**
     * 键
     */
    @ApiModelProperty("键对象")
    private K key;

    /**
     * 值
     */
    @ApiModelProperty("值对象")
    private V value;
}
