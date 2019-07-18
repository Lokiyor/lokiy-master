package com.lokiy.cloud.data.datasource.annotation;

import java.lang.annotation.*;

/**
 * @author Lokiy
 * @date 2019/7/18 20:41
 * @description: 用于方法上,动态指定数据源
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {
    String value();
}
