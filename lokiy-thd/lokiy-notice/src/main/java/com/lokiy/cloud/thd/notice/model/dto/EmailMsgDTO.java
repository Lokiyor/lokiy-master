package com.lokiy.cloud.thd.notice.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lokiy
 * @date 2020/3/17 16:43
 * @description:
 */
@Data
@ApiModel("邮件对象入参")
public class EmailMsgDTO {

    @ApiModelProperty("目标邮箱")
    private String toEmail;

    @ApiModelProperty("主题")
    private String subject;

    @ApiModelProperty("内容")
    private String content;
}
