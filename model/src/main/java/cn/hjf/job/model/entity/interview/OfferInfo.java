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
 * @date 2024-10-23
 */
@Data
@Schema(description = "OfferInfo")
@TableName("offer_info")
public class OfferInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "候选者id")
    @TableField("offer_info")
    private Long candidateId;

    @Schema(description = "职位id")
    @TableField("offer_info")
    private Long positionId;

    @Schema(description = "发送者id")
    @TableField("offer_info")
    private Long senderId;

    @Schema(description = "录用状态  0 待确认  1 已接受  2 已拒绝")
    @TableField("offer_info")
    private Integer offerStatus;

    @Schema(description = "报道地点")
    @TableField("offer_info")
    private String reportingLocation;

    @Schema(description = "最后报道时间")
    @TableField("offer_info")
    private LocalDateTime finalReportingTime;

    @Schema(description = "描述")
    @TableField("offer_info")
    private String offerDescription;
    }
