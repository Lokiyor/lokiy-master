package com.lokiy.cloud.common.util.math;

import com.lokiy.cloud.common.base.constant.GlobalConstant;

import java.math.BigDecimal;

/**
 * @author Lokiy
 * @date 2019/7/17 16:16
 * @description: 精度处理帮助类
 */
public class BigDecimalUtil {

    /**
     * 加法
     * @param v1 参数1
     * @param v2 参数2
     * @return 结果
     */
    public static BigDecimal add(String v1, String v2){
        BigDecimal b = new BigDecimal(v1);
        return b.add(new BigDecimal(v2));
    }


    /**
     * 减法
     * @param v1 参数1
     * @param v2 参数2
     * @return 结果
     */
    public static BigDecimal subtract(String v1, String v2){
        BigDecimal b = new BigDecimal(v1);
        return b.subtract(new BigDecimal(v2));
    }

    /**
     * 乘法
     * @param v1 参数1
     * @param v2 参数2
     * @return 结果
     */
    public static BigDecimal multiply(String v1, String v2){
        BigDecimal b = new BigDecimal(v1);
        return b.multiply(new BigDecimal(v2));
    }

    /**
     * 除法(保留两位小数)
     * @param v1 参数1
     * @param v2 参数2
     * @return 结果
     */
    public static BigDecimal divide(String v1, String v2){
        BigDecimal b = new BigDecimal(v1);
        return b.divide(new BigDecimal(v2), GlobalConstant.Number.TWO_INT, BigDecimal.ROUND_HALF_UP);
    }
}
