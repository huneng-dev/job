package cn.hjf.job.model.entity.application;

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
@Schema(description = "ApplicationStatusHistory")
@TableName("application_status_history")
public class ApplicationStatusHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "投递记录id")
    @TableField("application_status_history")
    private Long applicationId;

    @Schema(description = "状态 ")
    @TableField("application_status_history")
    private Integer status;

    @Schema(description = "操作人id")
    @TableField("application_status_history")
    private Long recruiterId;
    }
