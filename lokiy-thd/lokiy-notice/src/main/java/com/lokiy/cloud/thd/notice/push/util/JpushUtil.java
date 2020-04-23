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
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.push.model.notification.WinphoneNotification;
import com.lokiy.cloud.thd.notice.model.bo.JpushMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @author Lokiy
 * @date 2020/3/16 15:19
 * @description:  Alert-通知栏消息  Msg-自定义消息
 */
@Component
@Slf4j
public class JpushUtil {


    @Value("${notice.jg.app-key}")
    private String appKey;

    @Value("${notice.jg.master-secret}")
    private String masterSecret;

    /**
     * ios环境
     */
    @Value("${notice.jpush.ios-env}")
    private boolean iosEnv;




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
     * 自定义消息
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
     * @param jpushMsg
     * @param alias
     * @return
     */
    public boolean pushAlertByAlias(JpushMsg jpushMsg, String... alias) {
        return pushAlertByAudience(Audience.alias(alias), jpushMsg);
    }

    /**
     * 根据标签推送消息
     * @param jpushMsg
     * @param tag
     * @return
     */
    public boolean pushAlertByTag(JpushMsg jpushMsg, String... tag) {
        return pushAlertByAudience(Audience.tag(tag), jpushMsg);
    }

    /**
     * 根据用户推送消息
     * @param audience
     * @param jpushMsg
     * @return
     */
    public boolean pushAlertByAudience(Audience audience, JpushMsg jpushMsg) {
        return pushAlertPlatformAndAudience(Platform.all(), audience, jpushMsg);
    }


    /**
     * 推送所有
     * @param jpushMsg
     * @return
     */
    public boolean pushAlertToAll(JpushMsg jpushMsg) {
        return pushAlertByPlatform(Platform.all(), jpushMsg);
    }

    /**
     * 推送安卓
     * @param jpushMsg
     * @return
     */
    public boolean pushAlertToAndroid(JpushMsg jpushMsg) {
        return pushAlertByPlatform(Platform.android(), jpushMsg);
    }

    /**
     * 推送ios
     * @param jpushMsg
     * @return
     */
    public boolean pushAlertToIos(JpushMsg jpushMsg) {
        return pushAlertByPlatform(Platform.ios(), jpushMsg);
    }


    /**
     * 根据平台推送
     * @param platform
     * @param jpushMsg
     * @return
     */
    public boolean pushAlertByPlatform(Platform platform, JpushMsg jpushMsg) {
        return pushAlertPlatformAndAudience(platform, Audience.all(), jpushMsg);
    }

    /**
     * 根据平台和标签推送
     * @param platform
     * @param audience
     * @return
     */
    public boolean pushAlertPlatformAndAudience(Platform platform, Audience audience, JpushMsg jpushMsg) {
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(platform)
                .setAudience(audience)
                .setNotification(Notification.newBuilder()
                        .setAlert(jpushMsg.getContent())
                        .addPlatformNotification(
                                AndroidNotification.newBuilder()
                                        .setTitle(jpushMsg.getTitle())
                                        .setAlert(jpushMsg.getContent())
                                        .addExtras(jpushMsg.getExtras())
                                        .build())
                        .addPlatformNotification(
                                IosNotification.newBuilder()
                                        .setAlert(jpushMsg.getContent())
                                        .addExtras(jpushMsg.getExtras())
                                        .build())
                        .addPlatformNotification(
                                WinphoneNotification.newBuilder()
                                        .setTitle(jpushMsg.getTitle())
                                        .setAlert(jpushMsg.getContent())
                                        .addExtras(jpushMsg.getExtras())
                                        .build())
                        .build())
                .setOptions(Options.newBuilder().setApnsProduction(iosEnv).build())
                .build();
        return push(payload);
    }


    /**
     * 极光推送
     * @param payload
     * @return
     */
    public boolean push(PushPayload payload){
        JPushClient jPushClient = new JPushClient(masterSecret, appKey, null, ClientConfig.getInstance());
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
