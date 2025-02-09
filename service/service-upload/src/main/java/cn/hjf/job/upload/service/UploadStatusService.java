package cn.hjf.job.upload.service;

import cn.hjf.job.model.dto.upload.UploadStatusDTO;

public interface UploadStatusService {

    UploadStatusDTO getStatus(String fileHash);

    void updateStatus(String fileHash, int chunkIndex);
}
