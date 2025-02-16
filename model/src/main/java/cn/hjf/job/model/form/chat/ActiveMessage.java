package cn.hjf.job.model.form.chat;

import cn.hjf.job.model.document.chat.Message;
import cn.hjf.job.model.document.chat.RTCSessionDescriptionInit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "启动通话消息")
public class ActiveMessage {
    private Message message;
    private RTCSessionDescriptionInit rtcSessionDescriptionInit;
}
