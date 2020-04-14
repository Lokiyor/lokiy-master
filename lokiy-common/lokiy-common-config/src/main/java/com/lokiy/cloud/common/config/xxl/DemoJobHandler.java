package com.lokiy.cloud.common.config.xxl;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Lokiy
 * @date 2020/4/14 9:28
 * @description: 模板任务调度类
 */
@Component
@Slf4j
public class DemoJobHandler {


    /**
     * 简单任务示例（Bean模式）
     */
    @XxlJob("defaultDemoJobHandler")
    public ReturnT<String> defaultDemoJobHandler(String param) {
        XxlJobLogger.log("XXL-JOB, Hello World.");

        System.out.println("执行了defaultDemoJobHandler");
        log.info("执行了defaultDemoJobHandler");
        return ReturnT.SUCCESS;
    }
}
