package cn.hjf.job.model.entity.interview;

import cn.hjf.job.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
                                                                        /**
 * 
 * @author hjf
 * @date 2025-02-04
 */
@Data
@Schema(description = "Interview")
@TableName("interview")
public class Interview extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "关联简历投递记录")
    @TableField("delivery_id")
    private Long deliveryId;

    @Schema(description = "当前面试流程id")
    @TableField("current_process_id")
    private Long currentProcessId;

    @Schema(description = "状态：0 面试计划，1 面试中，2 面试通过 ，3 面试失败，4 面试取消")
    @TableField("status")
    private Integer status;
    }
