package cn.hjf.job.model.auth.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoForm {

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 用户密码（加密过的）
     */
    private String password;
}
