package com.lokiy.cloud.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author Lokiy
 * @date 2019/7/12 14:54
 * @description: eureka
 */
@EnableEurekaServer
@SpringBootApplication
public class LokiyEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(LokiyEurekaApplication.class, args);
    }

}
