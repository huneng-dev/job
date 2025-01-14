package cn.hjf.job.model.vo.resume;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "简历ES搜索结果")
public class ResumeVoEs {

    @Schema(description = "简历 id")
    private Long id;

    @Schema(description = "用户 id")
    private Long candidateId;

    @Schema(description = "用户姓氏")
    private String surname;

    @Schema(description = "求职状态")
    private Integer jobStatus;

    @Schema(description = "个人优势")
    private String personalAdvantages;

    @Schema(description = "专业技能")
    private String professionalSkills;

    @Schema(description = "期望职位 id")
    private Long expectedPositionId;

    @Schema(description = "行业 id")
    private Long industryId;

    @Schema(description = "城市")
    private String workCity;

    @Schema(description = "最低薪资")
    private Integer salaryMin;

    @Schema(description = "最高薪资")
    private Integer salaryMax;

    @Schema(description = "是否面议")
    private Integer isNegotiable;

    @Schema(description = "工作类型")
    private Integer jobType;

    @Schema(description = "学校名称")
    private String schoolName;

    @Schema(description = "专业")
    private String major;

    @Schema(description = "教育水平")
    private Integer educationLevel;

    @Schema(description = "开始年")
    private Integer startYear;

    @Schema(description = "结束年")
    private Integer endYear;
}
