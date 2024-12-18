package cn.hjf.job.model.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "公司员工信息")
public class EmployeeInfoVo {

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "姓名")
    private String name;
}
