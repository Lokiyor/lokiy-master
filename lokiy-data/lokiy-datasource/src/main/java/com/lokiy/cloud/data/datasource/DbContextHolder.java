package com.lokiy.cloud.data.datasource;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lokiy
 * @date 2019/7/18 20:42
 * @description: s数据源上下文
 */
@Slf4j
public class DbContextHolder {

    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 所有的数据源
     */
    public static List<String> dataSourceIds = new ArrayList<>();

    /**
     * 设置数据源
     * @param dbTypeEnum
     */
    public static void setDbType(String dbTypeEnum) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<");
        LocalDateTime now = LocalDateTime.now();
        CONTEXT_HOLDER.set(dbTypeEnum);
    }

    /**
     * 取得当前数据源
     * @return
     */
    public static String getDbType() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 清除上下文数据
     */
    public static void clearDbType() {
        CONTEXT_HOLDER.remove();
    }

    /**
     * 判断指定DataSrouce当前是否存在
     *
     * @param dataSourceId
     * @return
     */
    public static boolean containsDataSource(String dataSourceId) {
        return dataSourceIds.contains(dataSourceId);
    }
}
