package com.lokiy.cloud.common.base.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Lokiy
 * @date 2019/7/17 11:43
 * @description: error code enum
 */
@AllArgsConstructor
@Getter
public enum CodeEnum {

    /**
     * 成功标识
     */
    SUCCESS(200, "SUCCESS"),

    UNKNOWN_ERROR( 500, "未知错误"),


    /**
     * 极光相关错误信息
     */
    JG_SMS_SEND_ERROR(71000,"极光信息发送失败"),
    JG_SMS_CODE_SEND_ERROR(71100,"极光验证码短信发送失败"),
    JG_SMS_CODE_CHECK_ERROR(71100,"极光验证码短信验证失败"),
    ;

    private Integer code;

    private String msg;


    public static CodeEnum getErrorCodeEnum(Integer code) {
        for (CodeEnum ce : values()) {
            if (ce.code.equals(code)) {
                return ce;
            }
        }
        return UNKNOWN_ERROR;
    }
}
