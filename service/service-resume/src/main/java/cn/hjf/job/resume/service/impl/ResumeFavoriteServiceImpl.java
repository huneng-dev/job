package cn.hjf.job.resume.service.impl;

import cn.hjf.job.model.entity.resume.ResumeFavorite;
import cn.hjf.job.resume.mapper.ResumeFavoriteMapper;
import cn.hjf.job.resume.service.ResumeFavoriteService;
import cn.hjf.job.resume.service.ResumeInfoService;
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
 * @since 2025-01-07
 */
@Service
public class ResumeFavoriteServiceImpl extends ServiceImpl<ResumeFavoriteMapper, ResumeFavorite> implements ResumeFavoriteService {

    @Resource
    private ResumeFavoriteMapper resumeFavoriteMapper;

    @Resource
    private ResumeInfoService resumeInfoService;

    @Override
    public Boolean getResumeFavoriteStatus(Long resumeId, Long userId) {
        LambdaQueryWrapper<ResumeFavorite> resumeFavoriteLambdaQueryWrapper = new LambdaQueryWrapper<>();
        resumeFavoriteLambdaQueryWrapper.eq(ResumeFavorite::getRecruiterId, userId).eq(ResumeFavorite::getResumeId, resumeId);

        ResumeFavorite resumeFavorite = resumeFavoriteMapper.selectOne(resumeFavoriteLambdaQueryWrapper);

        return resumeFavorite != null;
    }

    @Override
    public Boolean favoriteResume(Long resumeId, Long userId) {
        // 判断简历是否存在
        Boolean resumeExist = resumeInfoService.isResumeExist(resumeId);
        if (resumeExist == null || !resumeExist) return false; // 简历不存在 返回 false

        // 关注
        ResumeFavorite resumeFavorite = new ResumeFavorite();
        resumeFavorite.setRecruiterId(userId);
        resumeFavorite.setResumeId(resumeId);

        int insert = resumeFavoriteMapper.insert(resumeFavorite);
        return insert == 1;
    }

    @Override
    public Boolean cancelResumeFavorite(Long resumeId, Long userId) {
        LambdaQueryWrapper<ResumeFavorite> resumeFavoriteLambdaQueryWrapper = new LambdaQueryWrapper<>();
        resumeFavoriteLambdaQueryWrapper.eq(ResumeFavorite::getRecruiterId, userId).eq(ResumeFavorite::getResumeId, resumeId);
        int delete = resumeFavoriteMapper.delete(resumeFavoriteLambdaQueryWrapper);
        return delete == 1;
    }
}
