package cn.hjf.job.model.dto.upload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "上传状态")
public class UploadStatusDTO {
    private String fileHash;
    private String uploadId;
    private Set<Integer> completedChunks = new ConcurrentSkipListSet<>();
    private String objectName;
    private LocalDateTime expireTime;
}
