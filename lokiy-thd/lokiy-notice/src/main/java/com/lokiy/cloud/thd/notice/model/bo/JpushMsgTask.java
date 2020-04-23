package com.lokiy.cloud.thd.notice.model.bo;

import com.lokiy.cloud.common.util.generator.SnowFlakeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * @author Lokiy
 * @date 2020/4/23 0023
 * @description: 延迟队列业务类
 */
@Data
@ApiModel("延迟队列业务类")
public class JpushMsgTask {

    @ApiModelProperty("随机生成的消息id,标识")
    private Long msgId;


    @ApiModelProperty("推送对象")
    private List<String> tags;

    @ApiModelProperty("需推送的内容")
    private JpushMsg jpushMsg;


    public JpushMsgTask(JpushMsg jpushMsg, String... tagArr){
        this.msgId = SnowFlakeUtil.getFlowIdInstance().nextId();
        this.jpushMsg = jpushMsg;
        this.tags = Arrays.asList(tagArr);
    }
}
