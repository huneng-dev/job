package cn.hjf.job.model.vo.company;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "公司员工信息")
public class CompanyEmployeeVo {

    @Schema(description = "用户 id")
    private Long id;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "职称")
    private String title;
}
