package cn.hjf.job.sms.service.impl;

import cn.hjf.job.sms.config.SmsCodeProperties;
import cn.hjf.job.sms.service.SmsCodeService;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.tea.TeaException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class SmsCodeServiceImpl implements SmsCodeService {

    @Resource
    private Client client;

    @Resource
    private SmsCodeProperties smsCodeProperties;

    @Override
    public boolean sendSmsCode(String phone, String code, String time) {

        // 使用 Map 构建模板参数，避免字符串拼接的问题
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        params.put("time", time);

        // 将 Map 转换为 JSON 字符串
        String templateParam;
        try {
            templateParam = new ObjectMapper().writeValueAsString(params);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize template parameters: {}", params, e);
            return false;
        }

        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName(smsCodeProperties.getSignName())
                .setTemplateCode(smsCodeProperties.getTemplateCode())
                .setPhoneNumbers(phone)
                .setTemplateParam(templateParam);

        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();

        try {
            client.sendSmsWithOptions(sendSmsRequest, runtime);
            return true;
        } catch (TeaException error) {
            // 错误 message
            System.out.println(error.getMessage());
            // 诊断地址
            System.out.println(error.getData().get("Recommend"));
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 此处仅做打印展示，请谨慎对待异常处理，在工程项目中切勿直接忽略异常。
            // 错误 message
            System.out.println(error.getMessage());
            // 诊断地址
            System.out.println(error.getData().get("Recommend"));
            com.aliyun.teautil.Common.assertAsString(error.message);
        }
        return false;
    }
}
