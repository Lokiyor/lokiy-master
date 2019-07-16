package com.lokiy.cloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author Lokiy
 * @date 2019/7/15 19:54
 * @description: gateway
 */
@SpringCloudApplication
public class LokiyGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(LokiyGatewayApplication.class, args);
    }

}
