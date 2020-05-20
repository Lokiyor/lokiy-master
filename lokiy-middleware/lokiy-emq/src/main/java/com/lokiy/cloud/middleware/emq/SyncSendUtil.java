package com.lokiy.cloud.middleware.emq;

import com.lokiy.cloud.common.util.generator.IdGeneratorUtil;
import com.lokiy.cloud.middleware.emq.enums.EmqQosEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SyncSendUtil {
	
	@Resource
	private MqttConfig config;
	
	private static int MAXSIZE = 16;

	private static Map<Integer, SyncSendClient> sendFactory = new ConcurrentHashMap<Integer, SyncSendClient>(MAXSIZE, 1);
	
	private int index = 0 ;
	
	@PostConstruct
	public void init() {
		if (StringUtils.isEmpty(config.getHost())) {
			return;
		}
		for (int i = 0; i < MAXSIZE; i++) {
			SyncSendClient client = new SyncSendClient();
			client.init(IdGeneratorUtil.getUUID32(), config.getHost());
			sendFactory.put(i, client);
		}
	}
	
	
	public void pubTopic(String topic, String message, EmqQosEnum qos) {
		sendFactory.get(index++ % 16).pubTopic(topic, message, qos);
	}
}
