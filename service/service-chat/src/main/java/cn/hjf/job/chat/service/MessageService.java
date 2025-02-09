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

    /**
     * 设置消息为已读状态
     * 此方法通过指定的聊天ID和发送者ID来标记消息为已读它首先构造一个查询条件，以找到所有相关的未读消息，
     * 然后使用MongoDB的updateMulti方法将这些消息的状态更新为"READ"同时，此方法记录了更新消息的数量，
     * 并将其作为日志信息的一部分输出最后，方法根据是否有消息被成功标记为已读返回一个布尔值
     *
     * @param chatId   聊天的唯一标识符，用于确定哪条聊天记录的消息需要被标记为已读
     * @param senderId 消息发送者的唯一标识符，与chatId一起用于精确查找需要标记的消息
     * @return 如果有消息被成功标记为已读，则返回true；否则返回false
     */
    Boolean setReadMessage(Long chatId, Long senderId);
}
