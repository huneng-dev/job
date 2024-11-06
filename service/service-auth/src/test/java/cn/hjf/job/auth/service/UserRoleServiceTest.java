package cn.hjf.job.auth.service;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRoleServiceTest {
    @Resource
    private UserRoleService userRoleService;

    @Test
    public void getUserRoleById() {
        System.out.println(userRoleService.getUserRoleById(2L));
    }
}
