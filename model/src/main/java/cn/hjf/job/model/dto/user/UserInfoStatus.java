package cn.hjf.job.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoStatus {
    private Long id;      // 用户ID
    private Integer status; // 用户状态（例如：激活、禁用等）
}
