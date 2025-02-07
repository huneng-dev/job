package cn.hjf.job.chat.service.impl;

import cn.hjf.job.chat.service.NotificationService;
import cn.hjf.job.common.logging.core.LogUtils;
import cn.hjf.job.common.rabbit.constant.MqConst;
import cn.hjf.job.common.rabbit.service.RabbitService;
import cn.hjf.job.model.document.chat.Message;
import cn.hjf.job.model.entity.chat.MessageWrapper;
import cn.hjf.job.model.entity.chat.MessageWrapperType;
import cn.hjf.job.model.vo.chat.ChatRelationshipVo;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Resource
    private RabbitService rabbitService;

    @Override
    @Async
    public void sendCreateNotification(String userId, ChatRelationshipVo chatRelationshipVo) {
        try {
            // 1. 准备消息
            MessageWrapper<ChatRelationshipVo> messageWrapper = new MessageWrapper<>();
            messageWrapper.setMessageWrapperType(MessageWrapperType.CREATE_CHAT);
            messageWrapper.setData(chatRelationshipVo);

            // 2. 发送消息
            String userQueueName = MqConst.STOMP_USER_QUEUE_PREFIX + userId;
            rabbitService.sendMessageToQueue(userQueueName, messageWrapper);
            LogUtils.info("发送创建聊天关系给指定的用户", userId, chatRelationshipVo);
        } catch (Exception e) {
            LogUtils.error("发送创建聊天关系失败", userId, chatRelationshipVo);
        }
    }

    @Override
    @Async
    public void sendMessageNotification(Message message) {
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

    @Override
    @Async
    public void sendReadNotification(Long chatId, Long senderId) {
        try {
            // 1. 准备消息
            MessageWrapper<Long> messageWrapper = new MessageWrapper<>();
            messageWrapper.setMessageWrapperType(MessageWrapperType.READ);
            messageWrapper.setData(chatId);

            // 2. 发送消息
            String userQueueName = MqConst.STOMP_USER_QUEUE_PREFIX + senderId;
            rabbitService.sendMessageToQueue(userQueueName, messageWrapper);
            LogUtils.info("发送已读消息给指定的用户", senderId, chatId);
        } catch (Exception e) {
            LogUtils.error("发送已读消息失败", senderId, chatId);
        }
    }
}
