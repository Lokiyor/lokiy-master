package com.lokiy.cloud.middleware.emq.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Lokiy
 * @date 2020/5/20 16:01
 * @description: 定义监听的mqtt信息
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface MqttTopic {

    /**
     * 匹配的topic的名称
     * @return topic的名称
     */
    String topic() default "$SYS/brokers";

    /**
     * 通配符级别，0代表不是通配符,1代表存在`+`，2代表为`#`
     * @return 通配符的级别
     */
    int wildcard() default 0;

    /**
     * 系统自己使用，用于判断处理任务是否为遗漏状态，业务系统不需要关注
     * @return 是否处理
     */
    boolean omitted() default false;
}
