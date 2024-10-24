package cn.hjf.job.model.entity.position;

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
@Schema(description = "PositionBenefit")
@TableName("position_benefit")
public class PositionBenefit extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "职位id")
    @TableField("position_id")
    private Long positionId;

    @Schema(description = "公司福利id")
    @TableField("company_benefit_id")
    private Long companyBenefitId;
    }
