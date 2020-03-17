package com.lokiy.cloud.thd.notice.sms.util;

import com.google.common.collect.Maps;
import com.lokiy.cloud.common.base.model.other.BaseKeyValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lokiy
 * @date 2020/3/17 14:07
 * @description:
 */
public class JsmsHelper {


    /**
     * 获取填充模板
     * @param kvList
     * @return
     */
    public static Map<String, String> getTempPara(List<BaseKeyValue<String,String>> kvList) {
        Map<String, String> tempPara = Maps.newHashMap();
        kvList.forEach(kv -> tempPara.put(kv.getKey(), kv.getValue()));
        return tempPara;
    }

    /**
     * 填充自定义验证码
     * @param code 模板中占位符为{{code}}
     * @param ttl 模板中占位符为{{ttl}}
     * @return
     */
    public Map<String, String> getCodeTempPara(String code, Integer ttl){
        Map<String, String> tempPara = new HashMap<>(4);
        tempPara.put("code", code);
        tempPara.put("ttl", String.valueOf(ttl));
        return tempPara;
    }
}
