package com.lokiy.cloud.thd.notice.sms.util;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jsms.api.JSMSClient;
import cn.jsms.api.SendSMSResult;
import cn.jsms.api.ValidSMSResult;
import cn.jsms.api.common.model.SMSPayload;
import com.lokiy.cloud.common.base.enums.ErrorCodeEnum;
import com.lokiy.cloud.common.base.exception.JgException;
import com.lokiy.cloud.common.base.model.other.BaseKeyValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lokiy
 * @date 2019/8/15 14:55
 * @description: 极光短信帮助类
 */
@Component
@Slf4j
public class JsmsUtil {


    @Autowired
    private JSMSClient jsmsClient;


    /**
     * 根据填充字符发送短信
     * @param mobile
     * @param tempId
     * @param tempPara
     * @return
     */
    public boolean sendByPara(String mobile, Integer tempId, Map<String, String> tempPara)  {
        SMSPayload smsPayload = SMSPayload.newBuilder()
                .setMobileNumber(mobile)
                .setTempId(tempId)
                .setTempPara(tempPara)
                .build();
        return send(smsPayload);
    }



    /**
     * 根据极光模板id发送信息
     * @param mobile
     * @param tempId
     * @return
     */
    public boolean sendByTempId(String mobile, Integer tempId) {
        SMSPayload smsPayload = SMSPayload.newBuilder()
                .setMobileNumber(mobile)
                .setTempId(tempId)
                .build();
        return send(smsPayload);
    }

    /**
     * 发送短信
     * @param payload
     * @return
     */
    public boolean send(SMSPayload payload) {

        try {
            SendSMSResult smsResult = jsmsClient.sendTemplateSMS(payload);
            log.info("发送短信结果,ResponseCode:{} ,{}", smsResult.getResponseCode(), smsResult.getMessageId());
            if(smsResult.isResultOK()){
                return true;
            }
            log.error("发送短信失败!");
        } catch (APIConnectionException | APIRequestException e) {
            e.printStackTrace();
            log.error("发送短信发生错误,{}",e.getMessage(), e);
        }
        return false;
    }







    /**
     * 生成发送短信验证码
     * @param mobile
     * @param tempId
     * @return
     */
    public String generateCode(String mobile, Integer tempId){
        SMSPayload smsPayload = SMSPayload.newBuilder()
                .setMobileNumber(mobile)
                .setTempId(tempId)
                .build();
        try {
            SendSMSResult smsResult = jsmsClient.sendSMSCode(smsPayload);
            if(smsResult.isResultOK()){
                log.info("{},短信发送成功！", mobile);
                return smsResult.getMessageId();
            }
            log.error("{},短信发送失败！,{}", mobile, smsResult.getResponseCode());
        } catch (APIConnectionException | APIRequestException e) {
            e.printStackTrace();
            log.error("发送验证码短信发生错误:{}", e.getMessage(), e);
        }
        throw new JgException(ErrorCodeEnum.JG_SMS_CODE_SEND_ERROR);
    }

    /**
     * 校验验证码
     * @param msgId 之前返回得messageId 可存在redis中
     * @param vCode 用户输入的code
     * @return
     */
    public boolean checkCode(String msgId, String vCode){
        try {
            ValidSMSResult validSmsResult = jsmsClient.sendValidSMSCode(msgId, vCode);
            return validSmsResult.getIsValid();
        } catch (APIConnectionException | APIRequestException e) {
            e.printStackTrace();
            log.error("验证验证码发生错误:{}", e.getMessage(), e);
        }
        throw new JgException(ErrorCodeEnum.JG_SMS_CODE_CHECK_ERROR);

    }

}
