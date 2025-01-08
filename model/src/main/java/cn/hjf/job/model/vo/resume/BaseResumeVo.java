package cn.hjf.job.model.vo.resume;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "应聘端基础简历信息")
public class BaseResumeVo {

    @Schema(description = "简历 id")
    private Long id;

    @Schema(description = "简历别名")
    private String resumeName;

    @Schema(description = "是否默认展示")
    private Integer isDefaultDisplay;


    @Schema(description = "工作城市")
    private String workCity;

    @Schema(description = "薪资")
    private Integer salaryMin;

    @Schema(description = "薪资")
    private Integer salaryMax;

    @Schema(description = "是否面议")
    private Integer isNegotiable;

    @Schema(description = "工作类型")
    private Integer JobType;

    @Schema(description = "基础项目列表")
    private List<BaseProjectExperienceVo> baseProjectExperienceVoList;

}
