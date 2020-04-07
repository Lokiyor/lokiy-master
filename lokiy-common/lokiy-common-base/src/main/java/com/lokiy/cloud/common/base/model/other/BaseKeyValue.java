package com.lokiy.cloud.common.base.model.other;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Lokiy
 * @date 2019/7/17 14:53
 * @description: 键值对对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseKeyValue<K, V> implements Serializable {

    /**
     * 键
     */
    private K key;

    /**
     * 值
     */
    private V value;
}
