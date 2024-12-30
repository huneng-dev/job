package cn.hjf.job.position.service;

import cn.hjf.job.model.entity.position.ApplicantFavorite;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hjf
 * @since 2024-10-25
 */
public interface ApplicantFavoriteService extends IService<ApplicantFavorite> {


    /**
     * 添加职位到喜欢
     *
     * @param positionId 职位 id
     * @param userId     用户 id
     * @return boolean 是否成功
     */
    boolean addPositionToFavorite(Long positionId, Long userId);

    /**
     * 删除喜欢的职位
     *
     * @param positionId 职位 id
     * @param userId     用户 id
     * @return 是否成功
     */
    boolean deletePositionFavorite(Long positionId, Long userId);

    /**
     * @param positionId 职位 id
     * @param userId     用户 id
     * @return 是否成功
     */
    boolean isPositionFavorite(Long positionId, Long userId);

}
