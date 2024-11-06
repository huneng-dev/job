package cn.hjf.job.auth;


import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.auth.test.UserInfoEntity;
import cn.hjf.job.model.auth.test.UserInfoForm;
import cn.hjf.job.user.client.UserInfoFeignClient;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestOpenFeign {

    @Resource
    private UserInfoFeignClient userInfoFeignClient;

    @Test
    public void test1(){
        UserInfoForm userInfoForm = new UserInfoForm("123", "123456");
        Result<UserInfoEntity> userInfoEntityResult = userInfoFeignClient.verifyUserInfo(userInfoForm);
        System.out.println(userInfoEntityResult);
    }
}
