package com.lokiy.cloud.common.base.model.resp;

import com.lokiy.cloud.common.base.enums.CodeEnum;
import com.lokiy.cloud.common.base.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Lokiy
 * @date 2019/7/17 11:06
 * @description: return result
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class R<T> implements Serializable {

    /**
     * 成功标记
     */
    private boolean success;

    /**
     * 编码
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;


    /**
     * 返回结果
     * @param data
     * @param <T>
     * @return
     */
    public static <T> R result(T data){
        return new RBuilder<T>()
                .success(true)
                .code(CodeEnum.SUCCESS.getCode())
                .msg(CodeEnum.SUCCESS.getMsg())
                .data(data)
                .build();
    }

    /**
     * 返回成功
     * @return
     */
    public static R success(){
        return new R();
    }

    /**
     * 返回标识
     * @param codeEnum
     * @return
     */
    public R codeEnum(CodeEnum codeEnum){
        this.code = codeEnum.getCode();
        this.msg = codeEnum.getMsg();
        return this;
    }

    /**
     * 返回错误信息
     * @param errorCode
     * @param msg
     * @return
     */
    public static R error(Integer errorCode, String msg){
        R result = new R();
        result.setCode(errorCode);
        result.setMsg(msg);
        return result;
    }

    /**
     * 返回错误信息
     * @param exception
     * @return
     */
    public static R error(BusinessException exception){
        R result = new R();
        result.setCode(exception.getCode());
        result.setMsg(exception.getMessage());
        return result;
    }
}
