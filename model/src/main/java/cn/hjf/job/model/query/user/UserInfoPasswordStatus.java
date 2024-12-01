package cn.hjf.job.model.query.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoPasswordStatus {
    private Long id;       // 用户ID
    private String password; // 用户密码
    private Integer status;  // 用户状态（例如：激活、禁用等）
}
