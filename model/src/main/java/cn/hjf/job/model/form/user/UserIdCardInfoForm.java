package cn.hjf.job.model.form.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用户身份证信息表单")
public class UserIdCardInfoForm {

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "性别  0:不便透露 1:男 2:女")
    private Integer gender;

    @Schema(description = "出生日期")
    private LocalDate birthday;

    @Schema(description = "身份证号")
    private String idcardNo;

    @Schema(description = "身份证地址")
    private String idcardAddress;

    @Schema(description = "身份证有效日期")
    private LocalDate idcardExpire;

    @Schema(description = "身份证正面 url")
    private String idcardFrontUrl;

    @Schema(description = "身份证背面 url")
    private String idcardBackUrl;

}
