package cn.hjf.job.chat.repository;


import cn.hjf.job.model.document.chat.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public interface MessageRepository extends MongoRepository<Message, String> {

    // 根据 chatId 获取最新的一条消息
    Optional<Message> findFirstByChatIdOrderByTimestampDesc(Long chatId);

    // 根据 chatId 和时间游标分页查询（正序/倒序通用）
    @Query(value = "{"
            + "chatId: ?0, "
            + "timestamp: { $lt: ?1 }"  // 获取比游标时间更早的消息
            + "}",
            sort = "{ timestamp: -1 }") // 按时间倒序排列
    List<Message> findEarlierMessages(
            @Param("chatId") Long chatId,
            @Param("beforeTime") Instant beforeTime,
            Pageable pageable);

    // 获取初始页最新消息
    @Query(value = "{ chatId: ?0 }", sort = "{ timestamp: -1 }")
    List<Message> findLatestMessages(
            @Param("chatId") Long chatId,
            Pageable pageable);

}
