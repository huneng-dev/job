package cn.hjf.job.model.document.chat;

public enum MessageType {
    TEXT,   // 文本消息
    IMAGE,  // 图片消息
    VIDEO,  // 视频消息
    AUDIO,  // 音频消息
    WORD,   // 文件类型
    EXCEL, PPT, PDF, ZIP, FILE,
    GET_RESUME, // 简历类型
    SEND_RESUME,
    DIALING,
    RINGING,
    ACTIVE,
    ENDED;
}
