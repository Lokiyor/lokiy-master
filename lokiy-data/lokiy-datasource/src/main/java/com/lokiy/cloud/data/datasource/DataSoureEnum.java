package com.lokiy.cloud.data.datasource;

/**
 * @author Lokiy
 * @date 2019/7/18 20:29
 * @description:
 */
public enum DataSoureEnum {

    /**
     * 数据库
     */
    MASTER("master"),

    SLAVE("slave"),
    ;

    private String datasourceName;

    DataSoureEnum(String datasourceName) {
        this.datasourceName = datasourceName;
    }
}
