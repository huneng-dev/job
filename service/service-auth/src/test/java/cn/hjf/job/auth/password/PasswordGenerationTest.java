package cn.hjf.job.auth.password;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordGenerationTest {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Test
    public void generationPassword() {
        String encode = passwordEncoder.encode("job@020902");
        System.out.println(encode);
    }
}
