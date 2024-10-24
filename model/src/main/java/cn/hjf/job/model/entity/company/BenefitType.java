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
@Schema(description = "BenefitType")
@TableName("benefit_type")
public class BenefitType extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "福利名称")
    @TableField("benefit_name")
    private String benefitName;

    @Schema(description = "福利描述")
    @TableField("benefit_description")
    private String benefitDescription;

    @Schema(description = "图标")
    @TableField("icon")
    private String icon;
    }
