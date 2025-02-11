package cn.hjf.job.interview.service;

import cn.hjf.job.model.entity.interview.ResumeDelivery;
import cn.hjf.job.model.vo.base.PageVo;
import cn.hjf.job.model.vo.chat.ChatRelationshipVo;
import cn.hjf.job.model.vo.interview.ResumeDeliveryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 简历投递记录表 服务类
 * </p>
 *
 * @author hjf
 * @since 2025-02-04
 */
public interface ResumeDeliveryService extends IService<ResumeDelivery> {

    /**
     * 投递简历
     *
     * @param resumeId           简历id
     * @param chatRelationshipVo 聊天关系
     * @return 是否投递成功
     */
    ResumeDeliveryVo candidateResumeDelivery(Long resumeId, ChatRelationshipVo chatRelationshipVo);

    /**
     * 获取招聘者id简历投递记录
     *
     * @param recruiterId 招聘者id
     * @param candidateId 简历id
     * @param positionId  职位id
     * @return 简历投递记录
     */
    ResumeDeliveryVo getRecruiterIdResumeDeliveryVoByCandidateIdAndPositionId(Long recruiterId, Long candidateId, Long positionId);

    /**
     * 获取简历投递记录
     *
     * @param candidateId 简历id
     * @param recruiterId 招聘者id
     * @param positionId  职位id
     * @return 简历投递记录
     */
    ResumeDeliveryVo getCandidateIdResumeDeliveryVoByRecruiterIdAndPositionId(Long candidateId, Long recruiterId, Long positionId);

    /**
     * 获取简历投递记录
     *
     * @param userId     用户id
     * @param positionId 职位id
     * @param resumeId   简历id
     * @return 简历投递记录
     */
    ResumeDeliveryVo getResumeDeliveryVoByResumeId(Long userId, Long positionId, Long resumeId);


    /**
     * 获取简历投递记录
     *
     * @param resumeDeliveryPage 简历投递记录分页
     * @param userId             用户id
     * @return 简历投递记录
     */
    PageVo<ResumeDeliveryVo> getResumeDeliveryVoPageByUserId(Page<ResumeDelivery> resumeDeliveryPage, Long userId);
}
