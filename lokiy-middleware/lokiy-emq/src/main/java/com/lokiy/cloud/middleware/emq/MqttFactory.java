package com.lokiy.cloud.middleware.emq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName MqttFactory
 * @Description: Mqtt工厂
 * @Author ljy88
 * @Date 2020/5/18 
 * @Version V1.0
 **/
@Component
@Slf4j
public class MqttFactory {

    @Autowired
    private Client asyncClient;

    @Autowired
    private Client syncClient;

    @Autowired
    private MqttConfig config;
}
