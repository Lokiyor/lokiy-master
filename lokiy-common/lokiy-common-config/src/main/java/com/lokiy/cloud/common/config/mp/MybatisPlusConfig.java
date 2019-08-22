package com.lokiy.cloud.common.config.mp;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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
public class MybatisPlusConfig {

    /**
     * 分页插件
     * @return
     */
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 逻辑删除
     *
     * @return
     */
    @Bean
    public ISqlInjector sqlInjector() {
        return new LogicSqlInjector();
    }

    /**
     *  sql执行效率插件
     * @return
     */
    @Bean
    @Profile({"dev","beta"})
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        performanceInterceptor.setMaxTime(1000);
        performanceInterceptor.setFormat(true);
        return performanceInterceptor;
    }


    @Bean
    public GlobalConfig globalConfig() {
        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        dbConfig.setLogicDeleteValue("1")
                .setLogicNotDeleteValue("0")
                .setIdType(IdType.INPUT)
                //表名下划线转驼峰
                .setTableUnderline(true)
                .setFieldStrategy(FieldStrategy.NOT_NULL);

        GlobalConfig conf = new GlobalConfig();
        conf.setDbConfig(dbConfig)
                .setSqlInjector(sqlInjector())
                .setMetaObjectHandler(new LokiyMetaObjectHandler())
                .setBanner(false);
        return conf;
    }

}