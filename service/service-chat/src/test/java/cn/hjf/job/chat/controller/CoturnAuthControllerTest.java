package cn.hjf.job.chat.controller;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CoturnAuthControllerTest {

    @Resource
    private CoturnAuthController coturnAuthController;

    @Test
    public void testGetCredentials() {
        long strTime = System.currentTimeMillis();
        for (int i = 0; i < 1000000000; i++) {
//            coturnAuthController.getCredentials(i);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("执行时间：" + (endTime - strTime) + "ms");
    }
}
