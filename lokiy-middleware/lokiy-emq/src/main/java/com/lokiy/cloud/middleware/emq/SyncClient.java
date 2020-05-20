package com.lokiy.cloud.middleware.emq;

import com.lokiy.cloud.middleware.emq.enums.EmqQosEnum;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

@Component
public class SyncClient extends Client {


	public static MqttClient mqttClient = null;

	private String clientId;
	
	private String url;
	
	private String username;


	@Override
	public void init(String clientId, String url, String username) {
		this.clientId = clientId;
		this.url = url;
		this.username = username;
		// 初始化连接设置对象
		mqttConnectOptions = new MqttConnectOptions();
		// 初始化MqttClient
		// true可以安全地使用内存持久性作为客户端断开连接时清除的所有状态
		mqttConnectOptions.setCleanSession(false);
		mqttConnectOptions.setAutomaticReconnect(false);
		// 设置连接超时
		mqttConnectOptions.setConnectionTimeout(30);
		// 设置最大链接数
		mqttConnectOptions.setMaxInflight(10000);
		if (!StringUtils.isEmpty(username)) {
			mqttConnectOptions.setUserName(username);
		} 
		// 设置持久化方式
		memoryPersistence = new MemoryPersistence();
		
		try {
			mqttClient = new MqttClient(url, clientId, memoryPersistence);
		} catch (MqttException e) {
//			logger.error("初始化mqtt客户端失败.", e);
		}

		// 设置连接和回调
		if (null != mqttClient) {
			if (!mqttClient.isConnected()) {
				// 客户端添加回调函数
				mqttClient.setCallback(mqttReceiveCallback);
				// 创建连接
				try {
//					logger.info("开始创建mqtt链接.");
					mqttClient.connect(mqttConnectOptions);
				} catch (MqttException e) {
//					logger.error("创建mqtt链接失败.", e);
				}
			}
		} else {
//			logger.info("初始化mqtt客户端失败.");
		}
	}

	// 关闭连接
	@Override
	public void closeConnect() {
		closeMemoryPersistence();

		closeMqttClient();
	}

	private void closeMemoryPersistence() {
		// 关闭存储方式
		if (null != memoryPersistence) {
			try {
				memoryPersistence.close();
			} catch (MqttPersistenceException e) {
//				logger.error("Close memoryPersistence error.", e);
			}
		}
	}

	private void closeMqttClient() {
		if (null != mqttClient) {
			if (mqttClient.isConnected()) {
				try {
					mqttClient.disconnect();
				} catch (MqttException e) {
//					logger.error("Close mqttClient error.", e);
				}
			} else {
//				logger.info("mqttClient is not connect");
			}
			try {
				mqttClient.close();
			} catch (MqttException e) {
//				logger.error("Close mqttClient error.", e);
			}
		}
	}

	// 发布消息
	@Override
	public void pubTopic(String pubTopic, String message, EmqQosEnum qos) {
		if (null != mqttClient && mqttClient.isConnected()) {
			MqttMessage mqttMessage = new MqttMessage();
			mqttMessage.setQos(qos.getQosCode());
			mqttMessage.setPayload(message.getBytes());

			try {
				mqttClient.publish(pubTopic, mqttMessage);
			} catch (MqttException e) {
//				logger.error("发布mqtt消息失败.", e);
			}

		} else {
			reConnect();
		}

	}

	// 重新连接
	private void reConnect() {
		if (null != mqttClient) {
			if (!mqttClient.isConnected()) {
				if (null != mqttConnectOptions) {
					try {
						mqttClient.connect(mqttConnectOptions);
					} catch (MqttException e) {
//						logger.error("重连mqtt消息失败.", e);
					}
				} else {
//					logger.info("mqttConnectOptions is null");
				}
			} else {
//				logger.info("mqttClient is null or connect");
			}
		} else {
			init(clientId, url, username);
		}
	}

	// 订阅主题
	@Override
	public void subTopic(String topic) {
		if (null != mqttClient && mqttClient.isConnected()) {
			try {
//			    logger.info("开始订阅topic:{}",topic);
				mqttClient.subscribe(topic, EmqQosEnum.QOS_1.getQosCode());
//				logger.info("订阅topic:{}成功",topic);
			} catch (MqttException e) {
//				logger.error("订阅mqtt消息失败----报错,:{}",e.getMessage());
			}
		} else {
//			logger.error("订阅mqtt消息失败");
		}
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
}
