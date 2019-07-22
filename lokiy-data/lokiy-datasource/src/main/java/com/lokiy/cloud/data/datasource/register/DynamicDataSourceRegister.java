package com.lokiy.cloud.data.datasource.register;

import com.lokiy.cloud.common.base.constant.GlobalConstant;
import com.lokiy.cloud.data.datasource.DbContextHolder;
import com.lokiy.cloud.data.datasource.rout.DynamicDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lokiy
 * @date 2019/7/19 17:28
 * @description:
 */
@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties(DataSourceProperties.class)
@Slf4j
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    /**
     * 指定默认数据源
     */
    private static final String DATASOURCE_TYPE_DEFAULT = "com.alibaba.druid.pool.DruidDataSource";
    /**
     * 默认数据源
     */
    private DataSource defaultDataSource;
    /**
     * 其他数据源
     */
    private Map<String, DataSource> customDataSources = new HashMap<>();

    @Override
    public void setEnvironment(Environment environment) {
        log.info("-------------------------DynamicDataSourceRegister.setEnvironment-------------------------");
        initDefaultDataSource(environment);
        initCustomDataSources(environment);
    }

    /**
     * 加载主数据源
     * @param env
     */
    private void initDefaultDataSource(Environment env) {
        Map<String, Object> dsMap = new HashMap<>(8);
        dsMap.put("driver-class-name", env.getProperty("spring.datasource.driver-class-name"));
        dsMap.put("url", env.getProperty("spring.datasource.url"));
        dsMap.put("username", env.getProperty("spring.datasource.username"));
        dsMap.put("password", env.getProperty("spring.datasource.password"));
        defaultDataSource = buildDataSource(dsMap);

    }


    /**
     * 其他数据源
     * @param env
     */
    private void initCustomDataSources(Environment env) {
        // 读取配置文件获取更多数据源
        String dsPrefixs = env.getProperty("custom.datasource.names");
        if(StringUtils.isBlank(dsPrefixs)){
            return;
        }
        for (String dsPrefix : dsPrefixs.split(GlobalConstant.Symbol.COMMA)) {
            // 多个数据源
            Map<String, Object> dsMap = new HashMap<>(8);
            dsMap.put("driver-class-name", env.getProperty("custom.datasource." + dsPrefix + ".driver-class-name"));
            dsMap.put("url", env.getProperty("custom.datasource." + dsPrefix + ".url"));
            dsMap.put("username", env.getProperty("custom.datasource." + dsPrefix + ".username"));
            dsMap.put("password", env.getProperty("custom.datasource." + dsPrefix + ".password"));
            DataSource ds = buildDataSource(dsMap);
            customDataSources.put(dsPrefix, ds);

        }
    }

    /**
     * 创造dataSource
     * @param dsMap
     * @return
     */
    private DataSource buildDataSource(Map<String, Object> dsMap) {
        try {
            Object type = dsMap.get("type");
            if(type == null) {
                type = DATASOURCE_TYPE_DEFAULT;
            }
            Class<? extends DataSource> dataSourceType;
            dataSourceType = (Class<? extends DataSource>) Class.forName((String) type);
            String driverClassName = dsMap.get("driver-class-name").toString();
            String url = dsMap.get("url").toString();
            String username = dsMap.get("username").toString();
            String password = dsMap.get("password").toString();
            // 自定义DataSource配置
            DataSourceBuilder factory = DataSourceBuilder.create().driverClassName(driverClassName).url(url)
                    .username(username).password(password).type(dataSourceType);
            return factory.build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        log.info("-------------------------DynamicDataSourceRegister.registerBeanDefinitions-------------------------");
        Map<Object, Object> targetDataSources = new HashMap<>(8);
        //将主数据源添加到更多数据源里
        targetDataSources.put("dataSource", defaultDataSource);
        DbContextHolder.dataSourceIds.add("dataSource");
        //添加更多的数据源
        targetDataSources.putAll(customDataSources);
        DbContextHolder.dataSourceIds.addAll(customDataSources.keySet());
        //创建DynamicDataSource
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        //添加属性：AbstractRoutingDataSource.defaultTargetDataSource
        mpv.addPropertyValue("defaultTargetDataSource", defaultDataSource);
        mpv.addPropertyValue("targetDataSources", targetDataSources);
        registry.registerBeanDefinition("dataSource", beanDefinition);

        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>动态数据源配置成功,其他数据源: {} <<<<<<<<<<<<<<<<<<<<<<<<<", customDataSources.size());
    }
}
