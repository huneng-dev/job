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
@Schema(description = "ResumeInfo")
@TableName("resume_info")
public class ResumeInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "应聘者id")
    @TableField("resume_info")
    private Long candidateId;

    @Schema(description = "仅用户可见")
    @TableField("resume_info")
    private String resumeName;

    @Schema(description = "求职状态 (默认：0 离职随时到岗)")
    @TableField("resume_info")
    private Integer jobStatus;

    @Schema(description = "个人优势")
    @TableField("resume_info")
    private String personalAdvantages;

    @Schema(description = "用户描述自己拥有的专业技能")
    @TableField("resume_info")
    private String professionalSkills;
    }
