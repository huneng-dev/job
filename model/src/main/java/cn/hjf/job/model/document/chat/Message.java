package cn.hjf.job.model.document.chat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "chat_messages")
@Data
@AllArgsConstructor
@NoArgsConstructor
@CompoundIndexes({
        @CompoundIndex(name = "chatId_timestamp_index", def = "{'chatId': 1, 'timestamp': -1}"),
        @CompoundIndex(
                name = "status_update_index",
                def = "{'chatId': 1, 'senderId': 1, 'status': 1}",
                background = true
        )

})
public class Message {

    @Id
    private String id;                     // 唯一标识符，MongoDB 默认生成

    @NotNull(message = "聊天关系ID不能为空")
    private Long chatId;                 // 聊天关系id

    @Indexed
    private Long senderId;               // 发送者id

    @Indexed
    private Long receiverId;             // 接收者id

    @Size(max = 512, message = "消息内容长度不能超过2048个字符")
    private String content;                // 消息内容

    @NotNull(message = "消息类型不能为空")
    private MessageType messageType;       // 消息类型 (text/image/file等)

    private String attachmentUrl;          // 附件路径 (系统中拼接成URL)

    private String status;                 // 消息状态 (send/received/read)

    private Instant timestamp;             // 服务器时间 (UTC时间)

    private boolean isRetracted;           // 是否撤回
}
