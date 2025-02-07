package cn.hjf.job.common.rabbit.service;


import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitService {

    @Resource
    private RabbitTemplate rabbitTemplate;

    //发送消息
    public boolean sendMessage(String exchange,
                               String routingKey,
                               Object message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        return true;
    }

    // 发送消息到指定的队列
    public boolean sendMessageToQueue(String queueName, Object message) {
        rabbitTemplate.convertAndSend(queueName, message);
        return true;
    }
}
