package com.lokiy.cloud.thd.notice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class LokiyNoticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(LokiyNoticeApplication.class, args);
    }

}
