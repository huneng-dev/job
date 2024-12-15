package cn.hjf.job.model.vo.company;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "地址信息")
public class AddressInfoVo {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "省")
    private String province;

    @Schema(description = "市")
    private String city;

    @Schema(description = "县 | 区")
    private String district;

    @Schema(description = "详细地址")
    private String address;
}
