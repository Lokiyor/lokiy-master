package com.lokiy.cloud.test;

import com.lokiy.cloud.data.datasource.register.DynamicDataSourceRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


/**
 * @author Lokiy
 * @date 2019/7/22 19:54
 * @description: test-app
 */
@SpringBootApplication
@Import(DynamicDataSourceRegister.class)
public class LokiyTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(LokiyTestApplication.class, args);
    }

}
