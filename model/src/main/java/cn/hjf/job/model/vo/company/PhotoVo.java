package cn.hjf.job.model.vo.company;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "照片")
public class PhotoVo {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "url 地址")
    private String fileUrl;
}
