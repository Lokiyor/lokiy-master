package com.lokiy.cloud.thd.notice.email.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @author Lokiy
 * @date 2020/3/17 15:23
 * @description:
 */
@Component
@Slf4j
public class EmailUtil {


    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private JavaMailSender mailSender;



    public boolean sendMail(String toEmail, String subject, String text) {
        return sendSimpleMail(toEmail, subject, text);
    }

    /**
     * 发送简单邮件
     * @param toEmail
     * @param subject
     * @param text
     * @return
     */
    public boolean sendSimpleMail(String toEmail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(text);
        try {
            mailSender.send(message);
            return true;
        } catch (MailException e) {
            e.printStackTrace();
            log.error("邮件发送失败：to {}, {}", toEmail, e.getMessage(), e);
        }
        return false;
    }


    /**
     * 添加附件发送
     * @param toEmail
     * @param subject
     * @param text
     * @param files
     * @return
     */
    public boolean sendAttachmentsMail(String toEmail, String subject, String text, File... files) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(text);
            // 文件名作为附件名称
            for (File f: files) {
                helper.addAttachment(f.getName(), f);
            }
            mailSender.send(mimeMessage);
            return true;
        } catch (MessagingException | MailException e) {
            e.printStackTrace();
            log.error("邮件发送错误：{}", e.getMessage(),e);
        }
        return false;
    }


}
