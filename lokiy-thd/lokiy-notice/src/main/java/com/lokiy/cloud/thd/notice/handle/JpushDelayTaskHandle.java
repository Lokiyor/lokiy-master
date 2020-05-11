package com.lokiy.cloud.thd.notice.handle;

import com.lokiy.cloud.middleware.redis.RedisUtil;
import com.lokiy.cloud.thd.notice.constant.NoticeRedisConsts;
import com.lokiy.cloud.thd.notice.model.bo.JpushMsgTask;
import com.lokiy.cloud.thd.notice.push.util.JpushUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Set;

/**
 * @ClassName JpushDelayTask
 * @Description: 针对推送频率 600/分钟 实现的延迟推送
 * @Author ljy88
 * @Date 2020/4/17 
 * @Version V1.0
 **/
@Component
@Slf4j
public class JpushDelayTaskHandle {

    /**
     * 频率限制
     */
    private static final long FREQUENCY_LIMIT = 600L;

    /**
     * 预留实时推送大小，用于处理延时队列时，也能实时处理推送
     */
    private static final int PRE_DEAL_LIMIT = 100;

    /**
     * 排序问题，放至前置时间 为1970-01-01 00:00:00
     */
    private static final long EARLY_TIME = 0L;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private JpushUtil jpushUtil;


    /**
     * 每一分钟消费一次缓存队列
     */
    @Scheduled(cron = "0 * * * * ?")
    public void dealDelayQueue(){
        if(!redisUtil.getLock(NoticeRedisConsts.MC_JPUSH_DELAY_QUEUE_DEAL_LOCK, NoticeRedisConsts.MC_JPUSH_DELAY_QUEUE_DEAL_LOCK_EXPIRE_TIME)){
            return;
        }

        log.info("JpushDelayTaskHandle.dealDelayQueue------>开始处理推送延迟队列,队列大小：{}", getDelayQueueCount());
        //延迟队列可处理数 预留100频率以放1分钟内再有推送
        int canDealCount = (int) (FREQUENCY_LIMIT - PRE_DEAL_LIMIT - getFrequencyCount());
        if(canDealCount <= 0){
            return;
        }
        Set<Object> jpushMsgTaskSet = redisUtil.zRange(NoticeRedisConsts.NOTICE_JPUSH_DELAY_QUEUE, 0, canDealCount);

        //筛选推送
        jpushMsgTaskSet.forEach( o -> {
            JpushMsgTask jpushMsgTask = (JpushMsgTask) o;
            //需要推送
            if( canImmediatePush(jpushMsgTask, EARLY_TIME)){
                boolean pushFlag = pushDelayQueue(jpushMsgTask);
                if(pushFlag){
                    //推送成功,去除延迟队列中此数据
                    redisUtil.zRemoveValue(NoticeRedisConsts.NOTICE_JPUSH_DELAY_QUEUE, jpushMsgTask);
                }
            }
        });
        redisUtil.del(NoticeRedisConsts.MC_JPUSH_DELAY_QUEUE_DEAL_LOCK);
    }



    /**
     * 判断是否能够立即推送
     * @return
     */
    public boolean canImmediatePush(JpushMsgTask jpushMsgTask, Long score){
        boolean flag = false;
        while (true) {
            //上操作锁
            if (redisUtil.getLock(NoticeRedisConsts.NOTICE_JPUSH_MIN_FREQUENCY_LOCK,
                    NoticeRedisConsts.NOTICE_JPUSH_MIN_FREQUENCY_LOCK_EXPIRE_TIME)) {
                long lSize = redisUtil.lGetListSize(NoticeRedisConsts.NOTICE_JPUSH_MIN_FREQUENCY);
                //如果频率队列小于限制,新增并直接推送
                if (lSize < FREQUENCY_LIMIT) {
                    //如果没有发送的消息,则增加过期时间，用于记60s内推送的频率，否则直接放入
                    if (lSize <= 0) {
                        redisUtil.lSet(NoticeRedisConsts.NOTICE_JPUSH_MIN_FREQUENCY, jpushMsgTask, NoticeRedisConsts.FREQUENCY_EXPIRE_TIME);
                    } else {
                        redisUtil.lSet(NoticeRedisConsts.NOTICE_JPUSH_MIN_FREQUENCY, jpushMsgTask);
                    }
                    flag = true;
                } else {
                    //如果频率大于限制,则放入缓存队列中,
                    putIntoDelayQueue(jpushMsgTask, score);
                }
                //去锁
                redisUtil.del(NoticeRedisConsts.NOTICE_JPUSH_MIN_FREQUENCY_LOCK);
                break;
            }
        }
        return flag;
    }

    /**
     * 不能推送放入延迟队列中
     * @return
     */
    public boolean putIntoDelayQueue(JpushMsgTask jpushMsgTask, Long score){
        //操作锁
//        while (true) {
//            if (redisUtil.getLock(NoticeRedisConsts.NOTICE_JPUSH_DELAY_QUEUE_LOCK,
//                    NoticeRedisConsts.NOTICE_JPUSH_DELAY_QUEUE_LOCK_EXPIRE_TIME)) {
        if (score == null) {
            //新加入的延迟推送,放置当前时间
            redisUtil.zAdd(NoticeRedisConsts.NOTICE_JPUSH_DELAY_QUEUE, jpushMsgTask, Instant.now().toEpochMilli());
        } else {
            //用于延迟队列中取出还是无法推送的任务，放置再队列的前置位置
            redisUtil.zAdd(NoticeRedisConsts.NOTICE_JPUSH_DELAY_QUEUE, jpushMsgTask, score);
        }
                //去锁
//                redisUtil.del(NoticeRedisConsts.NOTICE_JPUSH_DELAY_QUEUE_LOCK);
//                break;
//            }
//        }
        return true;
    }


    /**
     * 获取 当前时间可发送频率大小
     * @return
     */
    public long getFrequencyCount(){
        return redisUtil.lGetListSize(NoticeRedisConsts.NOTICE_JPUSH_MIN_FREQUENCY);
    }

    /**
     * 获取延迟队列大小
     * @return
     */
    public long getDelayQueueCount(){
        return redisUtil.zCard(NoticeRedisConsts.NOTICE_JPUSH_DELAY_QUEUE);
    }


    /**
     * 推送延迟任务
     * @param jpushMsgTask
     * @return
     */
    private boolean pushDelayQueue(JpushMsgTask jpushMsgTask){
        return jpushUtil.pushAlertByTag(jpushMsgTask.getJpushMsg(),
                    jpushMsgTask.getTags().toArray(new String[0]));
    }
}
