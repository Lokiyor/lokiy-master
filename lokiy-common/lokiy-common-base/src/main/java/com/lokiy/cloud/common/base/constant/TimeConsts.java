package com.lokiy.cloud.common.base.constant;

/**
 * @author Lokiy
 * @date 2020/3/17 14:05
 * @description: 时间相关常量
 */
public interface TimeConsts {

    /**
     * 1秒
     */
    int ONE_SECOND = 1;
    /**
     * 1分钟
     */
    int ONE_MINUTE = 60;

    /**
     * 1小时
     */
    int ONE_HOUR = 3600;

    /**
     * 1天
     */
    int ONE_DAY = 86400;



    /**
     * redis的锁的过期时间，单位秒
     */
    long REDIS_LOCK_EXPIRE = 60;

    /**
     * 1分钟的毫秒数
     */
    long MILLISECONDS_PER_MINUTE = 60 * 1000L;

    /**
     * 2099/12/31 23:59:59
     */
    long MAX_TIME = 4102415999000L;


}
