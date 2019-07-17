package com.lokiy.cloud.common.base.enums;

/**
 * @author Lokiy
 * @date 2019/7/17 11:43
 * @description: error code enum
 */
public enum ErrorCodeEnum {

    /**
     * 成功标识
     */
    SUCCESS(0, "SUCCESS"),

    UNKNOWN_ERROR( 500, "未知错误"),
    ;


    private Integer code;

    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    ErrorCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ErrorCodeEnum getErrorCodeEnum(Integer code) {
        for (ErrorCodeEnum ece : values()) {
            if (ece.code.equals(code)) {
                return ece;
            }
        }
        return null;
    }
}