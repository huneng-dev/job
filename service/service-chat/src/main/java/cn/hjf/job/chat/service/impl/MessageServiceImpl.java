package cn.hjf.job.chat.service.impl;

import cn.hjf.job.chat.repository.MessageRepository;
import cn.hjf.job.chat.service.MessageService;
import cn.hjf.job.model.document.chat.Message;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {

    private static final int MAX_PAGE_SIZE = 100;
    @Resource
    private MessageRepository messageRepository;

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
