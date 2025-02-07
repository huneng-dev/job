package cn.hjf.job.push.service;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WebSocketMessageServiceTest {

    @Resource
    private WebSocketMessageService webSocketMessageService;

    @Test
    public void sendPrivateMessage() {
        webSocketMessageService.sendPrivateMessage("29", "hahahahaha");
    }
}
