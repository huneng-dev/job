package cn.hjf.job.gateway.config;

import cn.hjf.job.common.whitelist.WhitelistConfig;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@SpringBootTest
public class WhitelistConfigTest {

    @Resource
    private WhitelistConfig whitelistConfig;

    @Test
    public void getWhitelistConfig(){
        Set<String> whitelist = whitelistConfig.getPath();
        System.err.println("白名单:"+whitelist);
    }
}
