package cn.hjf.job.model.entity.company;

import cn.hjf.job.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
/**
 * 
 * @author hjf
 * @date 2024-10-31
 */
@Data
@Schema(description = "CompanyAddress")
@TableName("company_address")
public class CompanyAddress extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "公司id")
    @TableField("company_id")
    private Long companyId;

    @Schema(description = "省")
    @TableField("province")
    private String province;

    @Schema(description = "市")
    @TableField("city")
    private String city;

    @Schema(description = "县 | 区")
    @TableField("district")
    private String district;

    @Schema(description = "详细地址")
    @TableField("address")
    private String address;

    @Schema(description = "经度")
    @TableField("longitude")
    private BigDecimal longitude;

    @Schema(description = "维度")
    @TableField("latitude")
    private BigDecimal latitude;
    }
