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
@Schema(description = "PositionStatusHistory")
@TableName("position_status_history")
public class PositionStatusHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "职位id")
    @TableField("position_id")
    private Long positionId;

    @Schema(description = "变更状态")
    @TableField("status")
    private Integer status;

    @Schema(description = "变更人id   null表示：系统")
    @TableField("operator_id")
    private Long operatorId;
    }
