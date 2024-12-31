package cn.hjf.job.model.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "招聘端个人信息")
public class RecruiterUserInfoVo {

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "真实姓名")
    private String name;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "职称名称")
    private String titleName;

    // .... 后续再说
}
