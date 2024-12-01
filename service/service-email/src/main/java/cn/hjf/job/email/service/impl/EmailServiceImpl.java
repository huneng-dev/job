package cn.hjf.job.email.service.impl;

import cn.hjf.job.email.service.IEmailService;
import cn.hjf.job.email.utils.CollectionUtil;
import com.alibaba.nacos.common.utils.StringUtils;
import jakarta.mail.internet.InternetAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class EmailServiceImpl implements IEmailService {

    @Autowired
    private JavaMailSender javaMailSender;//注入邮件工具类

    @Override
    public void send(String name, String form, String to, String subject, String content, Boolean isHtml, String cc, String bcc, List<File> files) {

        if (StringUtils.isAnyBlank(form, to, subject, content)) {
            throw new IllegalArgumentException("发送人,接收人,主题,内容均不可为空");
        }
        try {
            //true表示支持复杂类型
            MimeMessageHelper messageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage(), true);
            //邮件发信人
            messageHelper.setFrom(new InternetAddress(name + "<" + form + ">"));
            //邮件收信人
            messageHelper.setTo(to.split(","));
            //邮件主题
            messageHelper.setSubject(subject);
            //邮件内容
            messageHelper.setText(content, isHtml);
            //抄送
            if (!StringUtils.isEmpty(cc)) {
                messageHelper.setCc(cc.split(","));
            }
            //密送
            if (!StringUtils.isEmpty(bcc)) {
                messageHelper.setCc(bcc.split(","));
            }
            //添加邮件附件
            if (CollectionUtil.isNotEmpty(files)) {
                for (File file : files) {
                    messageHelper.addAttachment(file.getName(), file);
                }
            }
            // 邮件发送时间
            messageHelper.setSentDate(new Date());

            //正式发送邮件
            javaMailSender.send(messageHelper.getMimeMessage());

        } catch (Exception e) {
            throw new RuntimeException("邮件发送失败", e);
        }
    }


    @Override
    public void sendText(String form, String to, String subject, String content) {
        this.send("121JOB-注册验证码", form, to, subject, content, false, null, null, null);
    }

    @Override
    @Async
    public void sendHtml(String form, String to, String subject, String content) {
        try {
            this.send("121JOB", form, to, subject, content, true, null, null, null);
        } catch (Exception e) {
            log.error("邮件发送失败", e);
        }
    }


}


