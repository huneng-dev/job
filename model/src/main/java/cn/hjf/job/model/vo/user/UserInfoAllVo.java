package cn.hjf.job.model.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用户全部基础信息")
public class UserInfoAllVo {

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "真实姓名")
    private String name;

    @Schema(description = "性别  0:不便透露 1:男 2:女")
    private Integer gender;

    @Schema(description = "出生日期")
    private LocalDate birthday;
}
