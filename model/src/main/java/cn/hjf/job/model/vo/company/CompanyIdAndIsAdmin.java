package cn.hjf.job.model.vo.company;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "公司id和是否是管理员")
public class CompanyIdAndIsAdmin {

    @Schema(description = "公司id")
    private Long companyId;

    @Schema(description = "是否是管理员")
    private Integer isAdmin;
}
