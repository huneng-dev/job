package cn.hjf.job.model.entity.company;

import cn.hjf.job.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
                                                    /**
 * 
 * @author hjf
 * @date 2024-10-25
 */
@Data
@Schema(description = "CompanyBenefit")
@TableName("company_benefit")
public class CompanyBenefit extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "公司id")
    @TableField("company_id")
    private Long companyId;

    @Schema(description = "福利id")
    @TableField("benefit_id")
    private Long benefitId;
    }
