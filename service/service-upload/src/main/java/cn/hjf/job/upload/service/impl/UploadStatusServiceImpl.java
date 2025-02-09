package cn.hjf.job.upload.service.impl;

import cn.hjf.job.common.constant.RedisConstant;
import cn.hjf.job.model.dto.upload.UploadStatusDTO;
import cn.hjf.job.upload.service.UploadStatusService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UploadStatusServiceImpl implements UploadStatusService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public UploadStatusDTO getStatus(String fileHash) {
        return (UploadStatusDTO) redisTemplate.opsForValue().get(RedisConstant.UPLOAD_STATUS + fileHash);
    }

    @Override
    public void updateStatus(String fileHash, int chunkIndex) {
        redisTemplate.opsForSet().add(
                RedisConstant.UPLOAD_STATUS + fileHash + ":chunks",
                chunkIndex
        );
    }
}
