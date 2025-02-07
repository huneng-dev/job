package cn.hjf.job.chat.service.impl;

import cn.hjf.job.chat.repository.MessageRepository;
import cn.hjf.job.chat.service.MessageService;
import cn.hjf.job.common.logging.core.LogUtils;
import cn.hjf.job.model.document.chat.Message;
import com.mongodb.client.result.UpdateResult;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {

    private static final int MAX_PAGE_SIZE = 100;
    @Resource
    private MessageRepository messageRepository;

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public Message getLatestMessageByChatId(Long chatId) {
        Optional<Message> message = messageRepository.findFirstByChatIdOrderByTimestampDesc(chatId);
        return message.orElse(null);
    }

    @Override
    public List<Message> getHistoryMessageByChatId(Long chatId, Integer pageSize, Instant beforeTime) {
        // 参数校验与修正
        int validPageSize = Optional.ofNullable(pageSize)
                .map(size -> Math.min(size, MAX_PAGE_SIZE))
                .orElse(20);

        // 动态选择查询方法
        return (beforeTime == null) ?
                getInitialPage(chatId, validPageSize) :
                getNextPage(chatId, validPageSize, beforeTime);
    }

    @Override
    public Boolean setReadMessage(Long chatId, Long senderId) {

        // 构造查询条件，寻找所有未读且未被撤回的消息
        Criteria criteria = Criteria.where("chatId").is(chatId)
                .and("senderId").is(senderId)
                .and("status").is("SEND")
                .and("isRetracted").is(false);

        // 创建更新操作，将消息状态设置为"READ"
        Update update = new Update()
                .set("status", "READ");

        // 执行更新操作，标记多条消息为已读
        UpdateResult result = mongoTemplate.updateMulti(
                Query.query(criteria),
                update,
                Message.class
        );

        // 记录日志，包括聊天ID、发送者ID和被标记为已读的消息数量
        LogUtils.info("消息标记已读",
                "chatId", chatId,
                "senderId", senderId,
                "count", result.getModifiedCount());

        // 根据是否有消息被标记为已读返回结果
        return result.getModifiedCount() > 0;
    }

    private List<Message> getInitialPage(Long chatId, int pageSize) {
        return messageRepository.findLatestMessages(
                chatId,
                PageRequest.of(0, pageSize)
        );
    }

    private List<Message> getNextPage(Long chatId, int pageSize, Instant beforeTime) {
        return messageRepository.findEarlierMessages(
                chatId,
                beforeTime,
                PageRequest.of(0, pageSize)
        );
    }
}
