package cn.hjf.job.push.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WebSocketMessageService {


    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    /**
     * 发送私人消息
     */
    public void sendPrivateMessage(String userId, Object message) {
        try {
            // 使用 convertAndSendToUser 方法发送私人消息
            // 第一个参数是用户ID
            // 第二个参数是目标地址（不需要包含/user前缀）
            // 第三个参数是消息内容
            messagingTemplate.convertAndSendToUser(
                    userId,
                    "/queue/messages",  // 这里对应客户端订阅的地址
                    message
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void sendRecruiterMessage(String message) {
        messagingTemplate.convertAndSend("/topic/recruiter", message);
    }

    public void sendMessageToUser(String userId, String message) {
        messagingTemplate.convertAndSend("/queue/user-" + userId, message);
//        messagingTemplate.convertAndSendToUser(userId, "/queue/user-", message);
    }

    private MessageHeaders createHeaders(String userId) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("simpUser", userId);
        // 添加自定义头，确保消息正确路由
        headers.put("originalDestination", "/queue/user-");
        return new MessageHeaders(headers);
    }
}
