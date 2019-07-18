package com.lokiy.cloud.data.datasource;

/**
 * @author Lokiy
 * @date 2019/7/18 20:42
 * @description: s数据源上下文
 */
public class DbContextHolder {

    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 设置数据源
     * @param dbTypeEnum
     */
    public static void setDbType(String dbTypeEnum) {
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

}
