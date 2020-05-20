package com.lokiy.cloud.middleware.emq;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Lokiy
 * @date 2020/5/11 16:25
 * @description: mqtt配置
 */
@Component
public class MqttConfig {

    /**
     * mqtt的url
     */
    @Value("${mqtt.server.host}")
    private String host;

    /**
     * mqtt的clientId
     */
    @Value("${mqtt.server.client-id}")
    private String clientId;


    public String getHost() {
        return host;
    }

    public String getClientId() {
        return clientId;
    }
}
