package com.lokiy.cloud.common.base.exception;

import com.lokiy.cloud.common.base.enums.CodeEnum;

import java.io.Serializable;

/**
 * @author Lokiy
 * @date 2019/7/17 15:31
 * @description: 业务级异常
 */
public class BusinessException extends RuntimeException implements Serializable {

    /**
     * 异常编码
     */
    protected Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public BusinessException() {
        super();
    }

    public BusinessException(String msg) {
        super(msg);
    }

    public BusinessException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(Integer code, String msg){
        super(msg);
        this.code = code;
    }

    public BusinessException(Integer code, String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
        this.code = code;
    }

    public BusinessException(CodeEnum errorCodeEnum, Object... args) {
        super(String.format(errorCodeEnum.getMsg(), args));
        this.code = errorCodeEnum.getCode();
    }
}
