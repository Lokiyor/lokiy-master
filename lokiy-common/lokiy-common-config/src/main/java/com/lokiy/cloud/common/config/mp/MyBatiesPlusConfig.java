package com.lokiy.cloud.common.config.mp;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Lokiy
 * @date 2019/7/22 20:22
 * @description: mybatis-plus配置
 */
@EnableTransactionManagement
@Configuration
@MapperScan
@Slf4j
public class MyBatiesPlusConfig {


}
