package com.lokiy.cloud.data.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author Lokiy
 * @date 2019/7/18 20:44
 * @description: 数据源路由类
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    /**
     * 取得当前的数据源
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DbContextHolder.getDbType();
    }
}
