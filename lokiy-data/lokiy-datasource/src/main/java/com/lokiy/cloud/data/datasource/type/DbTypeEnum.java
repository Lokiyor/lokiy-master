package com.lokiy.cloud.data.datasource.type;

/**
 * @author Lokiy
 * @date 2019/7/18 20:29
 * @description:
 */
public enum DbTypeEnum {

    /**
     * 数据库
     */
    MASTER("master"),

    SLAVE("slave"),
    ;

    private String datasourceName;

    DbTypeEnum(String datasourceName) {
        this.datasourceName = datasourceName;
    }
}
