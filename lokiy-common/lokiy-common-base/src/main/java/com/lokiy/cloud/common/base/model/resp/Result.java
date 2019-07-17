package com.lokiy.cloud.common.base.model.resp;

import com.lokiy.cloud.common.base.exception.BusinessException;
import lombok.AllArgsConstructor;
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
public class Result<T> implements Serializable {

    private boolean success = true;

    private Integer businessCode = 1;

    private Integer errorCode = 1;

    private String msg = "";

    private T data;


    public static <T> Result result(T data){
       Result<T> result = new Result<>();
       result.setData(data);
       return result;
    }

    public static Result success(){
        return new Result();
    }

    public static Result error(Integer errorCode, String msg){
        Result result = new Result();
        result.setErrorCode(errorCode);
        result.setMsg(msg);
        return result;
    }

    public static Result error(BusinessException exception){
        Result result = new Result();
        result.setErrorCode(exception.getCode());
        result.setMsg(exception.getMessage());
        return result;
    }
}
