package cn.hjf.job.model.entity.chat;

import lombok.Getter;

@Getter
public enum MessageWrapperType {
    CHAT_MESSAGE("chat_message"),
    CREATE_CHAT("create_chat"),
    READ("read");

    MessageWrapperType(String recallAllMessageByReceiver) {
    }
}
