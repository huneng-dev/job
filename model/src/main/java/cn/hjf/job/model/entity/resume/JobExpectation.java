package cn.hjf.job.model.entity.resume;

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
@Schema(description = "JobExpectation")
@TableName("job_expectation")
public class JobExpectation extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "简历id")
    @TableField("job_expectation")
    private Long resumeId;

    @Schema(description = "期望职位id")
    @TableField("job_expectation")
    private Long expectedPositionId;

    @Schema(description = "期望城市（市）")
    @TableField("job_expectation")
    private String workCity;

    @Schema(description = "期望薪资最低值，单位：千元(K)")
    @TableField("job_expectation")
    private Integer salaryMin;

    @Schema(description = "期望薪资最高值，单位：千元(K)")
    @TableField("job_expectation")
    private Integer salaryMax;

    @Schema(description = "是否面议，0 不是，1 是")
    @TableField("job_expectation")
    private Integer isNegotiable;

    @Schema(description = "期望行业id")
    @TableField("job_expectation")
    private Long industryId;
    }
