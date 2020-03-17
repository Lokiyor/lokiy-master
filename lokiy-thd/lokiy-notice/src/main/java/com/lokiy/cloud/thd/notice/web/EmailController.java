package com.lokiy.cloud.thd.notice.web;

import com.lokiy.cloud.common.base.model.resp.Result;
import com.lokiy.cloud.thd.notice.email.util.EmailUtil;
import com.lokiy.cloud.thd.notice.model.dto.EmailMsgDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lokiy
 * @date 2020/3/17 16:40
 * @description:
 */
@RestController
@RequestMapping("/lokiy/notice/email")
@Slf4j
@AllArgsConstructor
@Api("邮件接口")
public class EmailController {

    private EmailUtil emailUtil;

    @RequestMapping("/send")
    @ApiOperation("发送邮件")
    public Result sendEmail(@RequestBody EmailMsgDTO emailMsgDTO){
        emailUtil.sendMail(
                emailMsgDTO.getToEmail(),
                emailMsgDTO.getSubject(),
                emailMsgDTO.getContent());
        return Result.success();
    }



}
