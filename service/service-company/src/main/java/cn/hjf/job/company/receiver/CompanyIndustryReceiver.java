package cn.hjf.job.company.receiver;

import cn.hjf.job.common.constant.RedisConstant;
import cn.hjf.job.common.rabbit.constant.MqConst;
import cn.hjf.job.company.mapper.CompanyIndustryMapper;
import cn.hjf.job.model.vo.company.IndustryVo;
import com.rabbitmq.client.Channel;

import jakarta.annotation.Resource;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class CompanyIndustryReceiver {

    @Resource
    private CompanyIndustryMapper companyIndustryMapper;

    @Resource
    private RedisTemplate<String, List<IndustryVo>> redisTemplate;


}
