package cn.hjf.job.resume.service;

import cn.hjf.job.model.entity.resume.ResumeFavorite;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hjf
 * @since 2025-01-07
 */
public interface ResumeFavoriteService extends IService<ResumeFavorite> {


    /**
     * 当前简历是否收藏
     *
     * @param resumeId 简历 id
     * @param userId   用户 id
     * @return 是否收藏
     */
    Boolean getResumeFavoriteStatus(Long resumeId, Long userId);

    /**
     * 收藏简历
     *
     * @param resumeId 简历 id
     * @param userId   用户 id
     * @return 是否成功
     */
    Boolean favoriteResume(Long resumeId, Long userId);


    Boolean cancelResumeFavorite(Long resumeId, Long userId);

}
