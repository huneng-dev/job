package cn.hjf.job.model.vo.resume;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "简历基本信息")
public class ResumeVo {

    @Schema(description = "简历 id")
    private Long id;

    @Schema(description = "应聘者id")
    private Long candidateId;

    @Schema(description = "仅用户可见")
    private String resumeName;

    @Schema(description = "求职状态 (默认：0 离职随时到岗 ...)")
    private Integer jobStatus;

    @Schema(description = "个人优势")
    private String personalAdvantages;

    @Schema(description = "用户描述自己拥有的专业技能")
    private String professionalSkills;

    @Schema(description = "是否默认展示(0:否,1:是 )")
    private Integer isDefaultDisplay;

}
