package cn.hjf.job.model.form.resume;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "基础简历表单")
public class BaseResumeForm {
    private ResumeInfoForm resumeInfoForm;
    private JobExpectationForm jobExpectationForm;
    private EducationBackgroundForm educationBackgroundForm;
}
