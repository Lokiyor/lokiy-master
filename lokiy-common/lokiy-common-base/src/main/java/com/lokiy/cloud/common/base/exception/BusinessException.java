package com.lokiy.cloud.common.base.exception;

import com.lokiy.cloud.common.base.enums.ErrorCodeEnum;

/**
 * @author Lokiy
 * @date 2019/7/17 15:31
 * @description: 业务级异常
 */
public class BusinessException extends RuntimeException{

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

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(Integer code, String message){
        super(message);
        this.code = code;
    }

    public BusinessException(int code, String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
        this.code = code;
    }

    public BusinessException(ErrorCodeEnum errorCodeEnum, Object... args) {
        super(String.format(errorCodeEnum.getMsg(), args));
        this.code = errorCodeEnum.getCode();
    }
}
