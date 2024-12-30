package cn.hjf.job.position.service.impl;

import cn.hjf.job.model.entity.position.ApplicantFavorite;
import cn.hjf.job.position.mapper.ApplicantFavoriteMapper;
import cn.hjf.job.position.service.ApplicantFavoriteService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hjf
 * @since 2024-10-25
 */
@Service
public class ApplicantFavoriteServiceImpl extends ServiceImpl<ApplicantFavoriteMapper, ApplicantFavorite> implements ApplicantFavoriteService {

    @Resource
    private ApplicantFavoriteMapper applicantFavoriteMapper;

    @Override
    public boolean addPositionToFavorite(Long positionId, Long userId) {
        // 直接插入
        ApplicantFavorite applicantFavorite = new ApplicantFavorite();
        applicantFavorite.setPositionId(positionId);
        applicantFavorite.setCandidateId(userId);

        int isSuccess = applicantFavoriteMapper.insert(applicantFavorite);
        return isSuccess == 1;
    }

    @Override
    public boolean deletePositionFavorite(Long positionId, Long userId) {
        LambdaQueryWrapper<ApplicantFavorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApplicantFavorite::getPositionId, positionId).eq(ApplicantFavorite::getCandidateId, userId);

        int isSuccess = applicantFavoriteMapper.delete(queryWrapper);
        return isSuccess >= 1;
    }

    @Override
    public boolean isPositionFavorite(Long positionId, Long userId) {
        LambdaQueryWrapper<ApplicantFavorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApplicantFavorite::getPositionId, positionId)
                .eq(ApplicantFavorite::getCandidateId, userId);

        return applicantFavoriteMapper.exists(queryWrapper);
    }
}
