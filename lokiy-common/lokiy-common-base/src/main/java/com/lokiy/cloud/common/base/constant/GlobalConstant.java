package com.lokiy.cloud.common.base.constant;

/**
 * @author Lokiy
 * @date 2019/7/17 12:52
 * @description: 全局常量
 */
public class GlobalConstant {


    /**
     * 数字常量
     */
    public interface Number {
        int ONE_INT = 1;
        int TWO_INT = 2;
        int THREE_INT = 3;
        int FOUR_INT = 4;
        int FIVE_INT = 5;
        int SIX_INT = 6;
        int SEVEN_INT = 7;
        int EIGHT_INT = 8;
        int NINE_INT = 9;
        int TEN_INT = 10;
        int HUNDRED_INT = 100;
        int THOUSAND_INT = 1000;
    }

    /**
     * 字符串数字
     */
    public interface StrNumber {
        String ZERO = "0";
        String ZERO_ONE = "01";
    }

    /**
     * 标点符号
     */
    public interface Symbol {
        String COMMA = ",";
        String SPOT = ".";
        String COLON = ":";
        String SPACE = " ";
        String SLASH = "/";

        String UNDER_LINE = "_";
        String SHORT_LINE = "-";

        String PER_CENT = "%";
        String AT = "@";
    }

    /**
     * 基础对象属性
     */
    public interface BaseEntityField {
        String ID = "id";
        String DEL_FLAG = "delFlag";
        String CREATE_USER = "createUser";
        String CREATE_TIME = "createTime";
        String UPDATE_USER = "updateUser";
        String UPDATE_TIME = "updateTime";
    }

    /**
     * 默认表示
     */
    public interface DefaultFlag {
       Integer DEFAULT_DEL_FLAG = 1;

    }
}
