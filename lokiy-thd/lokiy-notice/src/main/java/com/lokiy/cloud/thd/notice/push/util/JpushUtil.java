package com.lokiy.cloud.thd.notice.push.util;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import com.lokiy.cloud.thd.notice.model.bo.JpushMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author Lokiy
 * @date 2020/3/16 15:19
 * @description:  Alert-弹窗通知  Msg-通知栏消息
 */
@Component
@Slf4j
public class JpushUtil {


    @Autowired
    private JPushClient jPushClient;

    /**
     * ios环境
     */
    private boolean iosEnv = true;



    /**
     * 根据平台推送消息
     *
     * @param jpushMsg
     */
    public boolean pushMsg(JpushMsg jpushMsg) {
        return pushMsgByPlatform(Platform.all(), jpushMsg);
    }

    /**
     * 根据平台推送消息
     *
     * @param jpushMsg
     */
    public boolean pushMsgByPlatform(Platform platform, JpushMsg jpushMsg) {
        return pushMsgByPlatformAndTag(platform, jpushMsg);
    }


    /**
     * 根据标签推送消息
     *
     * @param tag 标签
     * @param jpushMsg
     */
    public boolean pushMsgByTag(JpushMsg jpushMsg, String... tag) {
        return pushMsgByPlatformAndTag(Platform.all(), jpushMsg, tag);
    }




    /**
     * 根据别名推送消息
     *
     * @param alias 别名
     * @param jpushMsg
     */
    public boolean pushMsgByAlias(JpushMsg jpushMsg, String... alias) {
        return pushMsgByPlatformAndAlias(Platform.all(), jpushMsg, alias);
    }


    /**
     * 根据平台和标签推送消息
     * @param platform
     * @param tag 标签
     * @param jpushMsg
     */
    public boolean pushMsgByPlatformAndTag(Platform platform, JpushMsg jpushMsg, String... tag) {
        Audience audience;
        if (tag.length > 0) {
            audience = Audience.tag(tag);
        } else {
            return false;
        }
        PushPayload payload = buildMsgPayload(platform, audience, jpushMsg);
        return push(payload);
    }



    /**
     * 根据平台和别名推送
     * @param platform
     * @param jpushMsg
     * @param alias
     * @return
     */
    public boolean pushMsgByPlatformAndAlias(Platform platform, JpushMsg jpushMsg, String... alias) {
        Audience audience;
        if (alias.length > 0) {
            audience = Audience.alias(alias);
        } else {
          return false;
        }
        PushPayload payload = buildMsgPayload(platform, audience, jpushMsg);
        return push(payload);
    }


    /**
     * 详细信息构造
     * @param platform
     * @param audience
     * @param jpushMsg
     * @return
     */
    private PushPayload buildMsgPayload(Platform platform, Audience audience, JpushMsg jpushMsg) {
        return PushPayload.newBuilder()
                .setPlatform(platform)
                .setAudience(audience)
                .setMessage(Message.newBuilder()
                        .setTitle(jpushMsg.getTitle())
                        .setMsgContent(jpushMsg.getContent())
                        .addExtras(jpushMsg.getExtras())
                        .build())
                .setOptions(Options.newBuilder().setApnsProduction(iosEnv).build())
                .build();
    }










    /**
     * 根据别名推送消息
     * @param content
     * @param alias
     * @return
     */
    public boolean pushAlertByAlias(String content, String... alias) {
        return pushAlertByAudience(Audience.alias(alias), content);
    }

    /**
     * 根据标签推送消息
     * @param content
     * @param tag
     * @return
     */
    public boolean pushAlertByTag(String content, String... tag) {
        return pushAlertByAudience(Audience.tag(tag), content);
    }

    /**
     * 根据用户推送消息
     * @param audience
     * @param content
     * @return
     */
    public boolean pushAlertByAudience(Audience audience, String content) {
        return pushAlertPlatformAndAudience(Platform.all(), audience, content);
    }


    /**
     * 推送所有
     * @param content
     * @return
     */
    public boolean pushAlertToAll(String content) {
        return pushAlertByPlatform(Platform.all(), content);
    }

    /**
     * 推送安卓
     * @param content
     * @return
     */
    public boolean pushAlertToAndroid(String content) {
        return pushAlertByPlatform(Platform.android(), content);
    }

    /**
     * 推送ios
     * @param content
     * @return
     */
    public boolean pushAlertToIos(String content) {
        return pushAlertByPlatform(Platform.ios(), content);
    }


    /**
     * 根据平台推送
     * @param platform
     * @param content
     * @return
     */
    public boolean pushAlertByPlatform(Platform platform, String content) {
        return pushAlertPlatformAndAudience(platform, Audience.all(), content);
    }

    /**
     * 根据平台和标签推送
     * @param platform
     * @param audience
     * @param content
     * @return
     */
    public boolean pushAlertPlatformAndAudience(Platform platform, Audience audience, String content) {
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(platform)
                .setAudience(audience)
                .setNotification(Notification.alert(content))
                .setOptions(Options.newBuilder().setApnsProduction(iosEnv).build())
                .build();
        return push(payload);
    }


    /**
     * 推送
     * @param payload
     * @return
     */
    public boolean push(PushPayload payload){
        try {
            PushResult pushResult = jPushClient.sendPush(payload);
            log.info("极光推送结果,ResponseCode:{} ,{}", pushResult.getResponseCode(), pushResult.getOriginalContent());
            if(pushResult.isResultOK()){
                return true;
            }
            log.error("极光推送失败!");
        } catch (APIConnectionException | APIRequestException e) {
            e.printStackTrace();
            log.error("极光推送发生错误,{}",e.getMessage(), e);
        }
        return false;
    }
}
