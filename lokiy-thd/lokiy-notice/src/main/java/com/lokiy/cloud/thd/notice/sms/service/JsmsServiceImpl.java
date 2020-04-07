package com.lokiy.cloud.thd.notice.sms.service;

import com.lokiy.cloud.thd.notice.sms.util.JsmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Lokiy
 * @date 2020/3/18 16:50
 * @description:
 */
@Service
@Slf4j
public class JsmsServiceImpl {

    @Autowired
    private JsmsUtil jsmsUtil;


    /**
     * 生成验证码
     * @param mobile
     * @return msg
     */
    public String getCode(String mobile){
        String s = jsmsUtil.generateCode(mobile,111);

        return s;
    }
}
