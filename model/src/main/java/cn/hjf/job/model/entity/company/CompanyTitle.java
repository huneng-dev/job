package cn.hjf.job.model.entity.company;

import cn.hjf.job.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hjf
 * @version 1.0
 * @description
 */
@Data
@Schema(description = "CompanyTitle")
@TableName("company_title")
@NoArgsConstructor
@AllArgsConstructor
public class CompanyTitle extends BaseEntity {

    @Schema(description = "公司id")
    @TableField("company_id")
    private Long companyId;

    @Schema(description = "职称名称")
    @TableField("title_name")
    private String titleName;
}
