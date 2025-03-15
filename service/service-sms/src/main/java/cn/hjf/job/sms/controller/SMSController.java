package cn.hjf.job.sms.controller;

import cn.hjf.job.common.result.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短信控制器
 *
 * @author hjf
 * @version 1.0
 * @description
 */
@RestController
@RequestMapping("/sms")
public class SMSController {

    @PostMapping("/send")
    public Result<String> sendSMS() {
        return Result.ok("发送成功");
    }
}
