package cn.hjf.job.model.vo.company;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "法人信息")
public class LegalPersonInfoVo  implements Serializable {

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "身份证号")
    private String idcardNo;

    @Schema(description = "出生日期")
    private LocalDate birthday;

    @Schema(description = "身份证有效日期")
    private LocalDate idcardExpire;

    @Schema(description = "地址")
    private String idcardAddress;

    @Schema(description = "性别 1:男 2:女")
    private Integer gender;

    @Schema(description = "身份证正面url")
    private String idcardFrontUrl;

    @Schema(description = "身份证反面url")
    private String idcardBackUrl;

}
