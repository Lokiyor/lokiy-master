package com.lokiy.cloud.thd.notice.config;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
import cn.jsms.api.JSMSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Lokiy
 * @date 2020/3/17 11:30
 * @description: 极光初始连接配置类
 */
@Configuration
public class JgClientConfig {

    @Value("${notice.jg.app-key}")
    private String appKey;

    @Value("${notice.jg.master-secret}")
    private String masterSecret;


    @Bean
    public JPushClient jPushClient(){
        return new JPushClient(
                masterSecret,
                appKey,
                null,
                ClientConfig.getInstance());
    }


    @Bean
    public JSMSClient jsmsClient(){
        return new JSMSClient(
                masterSecret,
                appKey);
    }

}
