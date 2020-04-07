package com.lokiy.cloud.common.base.exception;

import com.lokiy.cloud.common.base.enums.CodeEnum;

/**
 * @author Lokiy
 * @date 2020/3/17 11:56
 * @description: 极光异常
 */
public class JgException extends BusinessException {

    public JgException(Integer errCode, String msg) {
        super(errCode,msg);
    }

    public JgException(CodeEnum ec) {
        super(ec);
    }

}
