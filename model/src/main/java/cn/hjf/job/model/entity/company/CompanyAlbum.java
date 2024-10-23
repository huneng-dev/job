package cn.hjf.job.model.entity.company;

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
@Schema(description = "CompanyAlbum")
@TableName("company_album")
public class CompanyAlbum extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "公司id")
    @TableField("company_album")
    private Long companyId;

    @Schema(description = "标识是图片还是视频，0 表示图片，1 表示视频")
    @TableField("company_album")
    private Integer mediaType;

    @Schema(description = "存储图片或视频文件的 URL 地址")
    @TableField("company_album")
    private String fileUrl;

    @Schema(description = "照片描述")
    @TableField("company_album")
    private String fileDescription;

    @Schema(description = "视频预览图")
    @TableField("company_album")
    private String previewImageUrl;
    }
