package cn.hjf.job.upload.controller;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.dto.upload.UploadStatusDTO;
import cn.hjf.job.upload.service.FileUploadService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传文件控制器
 * 此接口下的上传为大文件分片
 *
 * @author hjf
 * @version 1.0
 * @description
 */

@RestController
@RequestMapping("/upload/file")
public class FileUploadController {

    @Resource
    private FileUploadService fileUploadService;

    // 获取上传状态
    @GetMapping("/status")
    public Result<UploadStatusDTO> getUploadStatus(
            @RequestParam String hash) {
        return Result.ok(fileUploadService.getUploadStatus(hash));
    }

    // 分片上传
    @PostMapping("/chunk")
    public Result<?> uploadChunk(
            @RequestParam("chunk") MultipartFile chunk,
            @RequestParam String hash,
            @RequestParam int index,
            @RequestParam int total) {

        fileUploadService.uploadChunk(hash, index, total, chunk);
        return Result.ok();
    }

    // 合并分片
    @PostMapping("/merge")
    public Result<String> mergeChunks(
            @RequestParam String hash) {
        String objectName = fileUploadService.mergeChunks(hash);
        return Result.ok(objectName);
    }
}
