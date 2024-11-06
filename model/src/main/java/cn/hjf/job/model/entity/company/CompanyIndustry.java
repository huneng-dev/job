package cn.hjf.job.model.entity.company;

import cn.hjf.job.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
                                                    /**
 * 
 * @author hjf
 * @date 2024-10-31
 */
@Data
@Schema(description = "CompanyIndustry")
@TableName("company_industry")
public class CompanyIndustry extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "行业名称")
    @TableField("industry_name")
    private String industryName;

    @Schema(description = "父行业ID，指向该表中的另一个行业的 id")
    @TableField("parent_id")
    private Long parentId;
    }
