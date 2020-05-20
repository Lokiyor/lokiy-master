package com.lokiy.cloud.middleware.emq.enums;

/**
 * @author Lokiy
 * @date 2020/5/19 13:37
 * @description: 消息级别
 */
public enum EmqQosEnum {

    /**
     * 发送一次，不保证是否到达
     */
    QOS_0(0, "发送一次，不保证是否到达"),

    /**
     * 至少发送一次，保证至少一次到达目的地，包可能重复
     */
    QOS_1(1, "至少发送一次，保证至少一次到达目的地，包可能重复"),

    /**
     * 仅一次，保证存在且仅存在一次信息到达目的地
     */
    QOS_2(2, "仅一次，保证存在且仅存在一次信息到达目的地");

    /**
     * qos的级别定义
     */
    private int qosCode;

    /**
     * 该级别的描述信息
     */
    private String desc;

    EmqQosEnum(int qosCode, String desc) {
        this.qosCode = qosCode;
        this.desc = desc;
    }

    public int getQosCode() {
        return qosCode;
    }

    public String getDesc() {
        return desc;
    }
}

