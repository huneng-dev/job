package cn.hjf.job.model.vo.resume;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "教育背景")
public class EducationBackgroundVo {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "简历id")
    private Long resumeId;

    @Schema(description = "学校名称")
    private String schoolName;

    @Schema(description = "专业名称")
    private String major;

    @Schema(description = "学历 0：不限|无 1：初中及以下  2：中专 3：高中 4：大专 5：本科 6：硕士 7：博士")
    private Integer educationLevel;

    @Schema(description = "是否全日制")
    private Integer isFullTime;

    @Schema(description = "开始时间")
    private Integer startYear;

    @Schema(description = "结束时间")
    private Integer endYear;
}
