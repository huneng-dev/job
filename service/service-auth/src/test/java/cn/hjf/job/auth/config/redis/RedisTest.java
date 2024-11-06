package cn.hjf.job.auth.config.redis;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author hjf
 * @version 1.0
 * @description redis 工作状态测试
 */

@SpringBootTest
public class RedisTest {

    @Resource
    private  RedisTemplate<String,String> redisTemplate;

    @Test
    public void saveRedisKVTest(){
        redisTemplate.opsForValue().set("redisKVTest1","redisKVTest1");
    }

    @Test
    public void getRedisKVTest(){
        String value = redisTemplate.opsForValue().get("redisKVTest1");
        System.out.println(value);
    }

}
