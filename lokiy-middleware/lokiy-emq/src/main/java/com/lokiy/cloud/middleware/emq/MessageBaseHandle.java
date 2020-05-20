package com.lokiy.cloud.middleware.emq;


import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @author Lokiy
 * @date 2020/5/19 10:36
 * @description: 抛出abs类，提供handle方法供使用方继承使用
 */
public abstract class MessageBaseHandle {

    /**
     * 处理逻辑
     * @param topic topic的名称
     * @param message message的内容
     */
    public abstract void handle(String topic, MqttMessage message);
}
