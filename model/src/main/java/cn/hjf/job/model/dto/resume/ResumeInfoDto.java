package cn.hjf.job.model.dto.resume;

import cn.hjf.job.model.vo.resume.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "简历信息")
public class ResumeInfoDto {

    private ResumeVo resumeVo;

    private EducationBackgroundVo educationBackgroundVo;

    private JobExpectationVo jobExpectationVo;

    private List<ProjectExperienceVo> projectExperienceVos;

    private List<WorkExperienceVo> workExperienceVos;

    private List<HonorAwardVo> honorAwardVos;

    private List<CertificationVo> certificationVos;
}
