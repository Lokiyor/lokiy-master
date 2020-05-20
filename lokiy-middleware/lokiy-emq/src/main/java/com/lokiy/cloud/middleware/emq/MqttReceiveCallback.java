package com.lokiy.cloud.middleware.emq;

import com.google.common.collect.Maps;
import com.lokiy.cloud.middleware.emq.annotation.MqttTopic;
import com.lokiy.cloud.middleware.emq.constant.WildcardConst;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author Lokiy
 * @date 2020/5/19 10:33
 * @description:
 */
@Component
@Slf4j
public class MqttReceiveCallback implements MqttCallback {


    @Autowired
    private List<MessageBaseHandle> handleList;

    @Autowired
    private DefaultMessageHandle defaultHandle;

    /**
     * 处理的无通配符的map
     */
    private Map<String, MessageBaseHandle> level0HandleMap;

    /**
     * 处理的处在`+`通配符的map
     */
    private Map<String, MessageBaseHandle> level1HandleMap;

    /**
     * 处理的处在`#`通配符的map
     */
    private Map<String, MessageBaseHandle> level2HandleMap;

    @Resource
    private Executor receiveExecutor;

    @Override
    public void connectionLost(Throwable arg0) {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken arg0) {

    }

    @PostConstruct
    public void initMap() {
        MqttTopic mt;
//        log.debug("开始加载topic信息");
        level0HandleMap = Maps.newHashMap();
        level1HandleMap = Maps.newHashMap();
        level2HandleMap = Maps.newHashMap();
        for (MessageBaseHandle handle : handleList) {
            mt = handle.getClass().getAnnotation(MqttTopic.class);
            if (null == mt) {
//                logger.info("Handle define none of topic, class is {}", handle.getClass());
                continue;
            }
            if (mt.omitted()) {
                continue;
            }

            switch (mt.wildcard()) {
                case WildcardConst.LEVEL_WITHOUT:
                    level0HandleMap.put(mt.topic().replace("$", "\\$"), handle);
                    break;
                case WildcardConst.LEVEL_WITH_RANK:
                    level1HandleMap.put(mt.topic().replace("$", "\\$").replaceAll("\\+", "\\.\\+").replace("#", ".*"), handle);
                    break;
                case WildcardConst.LEVEL_WITH_ANY:
                    // 直接将#号去除即可
                    level2HandleMap.put(mt.topic().replace("$", "\\$").replace("#", ""), handle);
                    break;
                default:
                    // 没匹配到任何，输出错误日志
//                    logger.error("未知的通配符定义");
            }
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // 先找无匹配关系的topic，看看是否存在处理逻辑
        receiveExecutor.execute(() -> {
            if (level0HandleMap.containsKey(topic)) {
                level0HandleMap.get(topic).handle(topic, message);
                return;
            }

            // 再找`+`通配符的topic，看看是否存在处理逻辑
            for (String key : level1HandleMap.keySet()) {
                if (topic.matches(key)) {
                    level1HandleMap.get(key).handle(topic, message);
                    return;
                }
            }

            // 再找`#`通配符的topic,看看是否存在处理逻辑
            for (String key : level2HandleMap.keySet()) {
                if (topic.concat("/").startsWith(key)) {
                    level2HandleMap.get(key).handle(topic, message);
                    return;
                }
            }
            defaultHandle.handle(topic, message);
        });
    }
}
