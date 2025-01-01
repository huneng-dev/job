package cn.hjf.job.model.vo.company;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "招聘端公司信息(管理员)")
public class CompanyInfoRecruiterVo {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "公司名称")
    private String companyName;

    @Schema(description = "公司logo")
    private String companyLogo;

    @Schema(description = "公司规模")
    private String companySize;

    @Schema(description = "公司行业")
    private String industry;

    @Schema(description = "公司描述")
    private String companyDescription;

    @Schema(description = "公司官网")
    private String companyWebsite;

    @Schema(description = "当前系统下公司中的人数量")
    private Integer count;
}
