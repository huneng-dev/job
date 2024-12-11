package cn.hjf.job.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户信息")
public class UserInfoQuery {

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "手机号(176****0000)")
    private String phone;

    @Schema(description = "邮箱(111****111@job.cn)")
    private String email;

    @Schema(description = "人脸识别认证状态 0:未认证  1：审核中 2：认证通过 -1：认证未通过")
    private Integer authStatus;
}
