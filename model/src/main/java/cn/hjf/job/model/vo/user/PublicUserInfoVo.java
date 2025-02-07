package cn.hjf.job.model.vo.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "公共用户信息")
public class PublicUserInfoVo {
    // 用户 ID
    private Long id;

    // 用户头像
    private String avatar;

    // 姓名
    private String name;
}
