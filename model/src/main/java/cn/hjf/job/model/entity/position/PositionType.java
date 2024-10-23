package cn.hjf.job.model.entity.position;

import cn.hjf.job.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
                                                                        /**
 * 
 * @author hjf
 * @date 2024-10-23
 */
@Data
@Schema(description = "PositionType")
@TableName("position_type")
public class PositionType extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "行业id")
    @TableField("position_type")
    private Long industryId;

    @Schema(description = "职位类型的名称")
    @TableField("position_type")
    private String typeName;

    @Schema(description = "职位类型的描述")
    @TableField("position_type")
    private String description;
    }
