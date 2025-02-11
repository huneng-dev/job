package cn.hjf.job.model.dto.resume;

import cn.hjf.job.model.vo.resume.EducationBackgroundVo;
import cn.hjf.job.model.vo.resume.JobExpectationVo;
import cn.hjf.job.model.vo.resume.ResumeVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "简历基本信息")
public class ResumeBaseDto {
    @Schema(description = "简历基本信息")
    private ResumeVo resumeVo;

    @Schema(description = "教育背景")
    private EducationBackgroundVo educationBackgroundVo;

    @Schema(description = "工作需求")
    private JobExpectationVo jobExpectationVo;
}
