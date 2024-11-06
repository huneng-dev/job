package cn.hjf.job.model.entity.interview;

import cn.hjf.job.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 
 * @author hjf
 * @date 2024-10-31
 */
@Data
@Schema(description = "OfferInfo")
@TableName("offer_info")
public class OfferInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "候选者id")
    @TableField("candidate_id")
    private Long candidateId;

    @Schema(description = "职位id")
    @TableField("position_id")
    private Long positionId;

    @Schema(description = "发送者id")
    @TableField("sender_id")
    private Long senderId;

    @Schema(description = "录用状态  0 待确认  1 已接受  2 已拒绝")
    @TableField("offer_status")
    private Integer offerStatus;

    @Schema(description = "报道地点")
    @TableField("reporting_location")
    private String reportingLocation;

    @Schema(description = "最后报道时间")
    @TableField("final_reporting_time")
    private LocalDateTime finalReportingTime;

    @Schema(description = "描述 (MongoDB)")
    @TableField("offer_description")
    private String offerDescription;
    }
