package cn.hjf.job.chat.service;

import cn.hjf.job.model.document.chat.Message;

import java.time.Instant;
import java.util.List;

/**
 * 消息服务
 *
 * @author hjf
 * @version 1.0
 * @description
 */

public interface MessageService {

    /**
     * 保存消息
     *
     * @param message 消息
     * @return 消息
     */
    Message saveMessage(Message message);

    /**
     * 根据聊天关系ID获取第一条消息
     *
     * @param chatId 聊天关系ID
     * @return 消息
     */
    Message getLatestMessageByChatId(Long chatId);

    /**
     * 根据聊天关系ID获取历史消息
     *
     * @param chatId     聊天关系ID
     * @param beforeTime 时间
     * @return 消息列表
     */
    List<Message> getHistoryMessageByChatId(Long chatId, Integer pageSize, Instant beforeTime);
}
