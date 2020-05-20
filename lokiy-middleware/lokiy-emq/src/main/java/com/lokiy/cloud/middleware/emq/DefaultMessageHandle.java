package com.lokiy.cloud.middleware.emq;

import com.lokiy.cloud.middleware.emq.annotation.MqttTopic;
import com.lokiy.cloud.middleware.emq.constant.WildcardConst;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

/**
 * @author Lokiy
 * @date 2020/5/20 9:45
 * @description: 日志记录
 */
@Component
@MqttTopic(topic = "#", wildcard = WildcardConst.LEVEL_WITHOUT, omitted = true)
@Slf4j
public class DefaultMessageHandle extends MessageBaseHandle {


    @Override
    public void handle(String topic, MqttMessage message) {
        // 接受消息，直接记录日志即可
//       log.info ("recerive message,topic is {}, qos is {}, message is {}", topic, message.getQos(),
//                new String(message.getPayload()));
    }
}
