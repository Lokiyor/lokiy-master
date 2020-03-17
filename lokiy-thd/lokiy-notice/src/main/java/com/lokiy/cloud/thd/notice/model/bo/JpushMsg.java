package com.lokiy.cloud.thd.notice.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Lokiy
 * @date 2020/3/16 16:19
 * @description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("极光详细信息封装")
public class JpushMsg {

    @ApiModelProperty("推送标题")
    private String title;

    @ApiModelProperty("推送内容")
    private String content;

    @ApiModelProperty("额外信息")
    private Map<String, String> extras;
}
