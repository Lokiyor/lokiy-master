package com.lokiy.cloud.common.util.generator;

import cn.hutool.core.util.StrUtil;

import java.util.UUID;

/**
 * @author Lokiy
 * @date 2020/5/20 17:29
 * @description: 32位id生成器
 */
public class UUIDUtil {
    /**
     * 获取32位uuid
     *
     * @return 32位uuid
     */
    public static String getUUID32() {
        return UUID.randomUUID().toString().replaceAll(StrUtil.DASHED, StrUtil.EMPTY);
    }
}
