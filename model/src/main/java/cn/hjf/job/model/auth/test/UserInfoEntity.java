package cn.hjf.job.model.auth.test;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoEntity {

    /**
     * 用户唯一凭证
     */
    private Long id;

    /**
     * 用户类型
     */
    private Integer type;

    /**
     * 状态 0：用户不存在 1：密码错误 2：用户禁用 3：密码校验成功
     */
    private Integer states;
}
