package cn.hjf.job.interview.service;

import cn.hjf.job.model.document.chat.Message;

/**
 * 通知服务
 *
 * @author hjf
 * @version 1.0
 * @description
 */
public interface NotificationService {

    /**
     * 发送简历投递通知
     *
     * @param userId  用户id
     * @param message 消息
     */
    void sendResumeDeliveryNotification(Long userId, Message message);
}
