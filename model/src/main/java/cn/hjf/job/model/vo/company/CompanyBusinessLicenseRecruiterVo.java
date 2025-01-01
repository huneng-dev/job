package cn.hjf.job.model.vo.company;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "招聘端营业执照(管理员)")
public class CompanyBusinessLicenseRecruiterVo {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "公司id")
    private Long companyId;

    @Schema(description = "公司名称")
    private String name;

    @Schema(description = "统一社会信用代码")
    private String licenseNumber;

    @Schema(description = "注册资本")
    private String capital;

    @Schema(description = "法人姓名")
    private String legalPerson;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "经营范围")
    private String businessScope;

    @Schema(description = "主体类型  示例值：有限责任公司")
    private String type;

    @Schema(description = "营业期限")
    private String period;

    @Schema(description = "成立日期")
    private String establishmentDate;

    @Schema(description = "登记日期")
    private String registrationDate;

    @Schema(description = "注册机构")
    private String registrationAuthority;
}
