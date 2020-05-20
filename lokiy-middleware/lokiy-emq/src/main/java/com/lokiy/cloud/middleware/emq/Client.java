package com.lokiy.cloud.middleware.emq;

import com.lokiy.cloud.middleware.emq.enums.EmqQosEnum;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Lokiy
 * @date 2020/5/19 10:06
 * @description: mqtt客户端类
 */
public abstract class Client {

    @Autowired
    protected MqttReceiveCallback mqttReceiveCallback;

    /**
     * 持久化方式
     */
    protected MemoryPersistence memoryPersistence = null;

    /**
     * 链接选项
     */
    protected MqttConnectOptions mqttConnectOptions = null;

    /**
     * 初始化mqtt链接
     *
     * @param clientId
     *            mqtt的clientid信息
     * @param url
     *            mqtt的链接地址
     * @param username
     *            链接的用户名
     */
    public abstract void init(String clientId, String url, String username);

    /**
     * 发布信息
     *
     * @param topic
     *            topic名称，不支持通配符
     * @param message
     *            消息
     * @param qos
     *            qos级别
     */
    public abstract void pubTopic(String topic, String message, EmqQosEnum qos);

    /**
     * 订阅消息
     *
     * @param topic
     *            topic名称，支持统配
     */
    public abstract void subTopic(String topic);

    /**
     * 关闭链接
     */
    abstract void closeConnect();
}
