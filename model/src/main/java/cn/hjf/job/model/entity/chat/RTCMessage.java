package cn.hjf.job.model.entity.chat;

import cn.hjf.job.model.document.chat.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "RTC 消息")
public class RTCMessage<T> {

    // 信令类型
    private String type;

    // 信令内容
    private T payload;

    // 当前维持的会话消息
    private Message message;
}
