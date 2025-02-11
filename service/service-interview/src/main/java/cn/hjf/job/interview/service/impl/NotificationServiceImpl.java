package cn.hjf.job.interview.service.impl;

import cn.hjf.job.common.logging.core.LogUtils;
import cn.hjf.job.common.rabbit.constant.MqConst;
import cn.hjf.job.common.rabbit.service.RabbitService;
import cn.hjf.job.interview.service.NotificationService;
import cn.hjf.job.model.document.chat.Message;
import cn.hjf.job.model.entity.chat.MessageWrapper;
import cn.hjf.job.model.entity.chat.MessageWrapperType;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Resource
    private RabbitService rabbitService;

    @Override
    @Async
    public void sendResumeDeliveryNotification(Long userId, Message message) {
        try {
            // 1. 准备消息
            MessageWrapper<Message> messageWrapper = new MessageWrapper<>();
            messageWrapper.setMessageWrapperType(MessageWrapperType.CHAT_MESSAGE);
            messageWrapper.setData(message);

            // 2. 发送消息
            String userQueueName = MqConst.STOMP_USER_QUEUE_PREFIX + message.getReceiverId();
            rabbitService.sendMessageToQueue(userQueueName, messageWrapper);
            LogUtils.info("发送消息给指定的用户", message.getReceiverId(), message);
        } catch (Exception e) {
            LogUtils.error("发送消息失败", message.getReceiverId(), message);
        }
    }
}
