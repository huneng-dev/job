package cn.hjf.job.chat.service;

import cn.hjf.job.model.document.chat.Message;
import cn.hjf.job.model.vo.chat.ChatRelationshipVo;

/**
 * 通知服务接口，用于定义发送通知的相关方法
 */
public interface NotificationService {

    /**
     * 发送创建通知
     * 当一个新地聊天关系建立时，通过此方法向指定用户发送通知
     *
     * @param userId             用户ID，表示通知的目标用户
     * @param chatRelationshipVo 聊天关系视图对象，包含了与聊天关系相关的信息
     */
    void sendCreateNotification(String userId, ChatRelationshipVo chatRelationshipVo);


    /**
     * 发送消息通知
     * 当有新消息到达时，通过此方法向指定用户发送通知
     *
     * @param message 消息对象，包含了与消息相关的信息
     */
    void sendMessageNotification(Message message);
}
