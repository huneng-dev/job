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
@Schema(description = "ApplicantFavorite")
@TableName("applicant_favorite")
public class ApplicantFavorite extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "职位id")
    @TableField("position_id")
    private Long positionId;

    @Schema(description = "应聘者id")
    @TableField("candidate_id")
    private Long candidateId;
    }
