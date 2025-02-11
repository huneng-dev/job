package cn.hjf.job.interview.service.impl;

import cn.hjf.job.interview.mapper.ResumeDeliveryMapper;
import cn.hjf.job.interview.service.ResumeDeliveryService;
import cn.hjf.job.model.entity.interview.ResumeDelivery;
import cn.hjf.job.model.vo.base.PageVo;
import cn.hjf.job.model.vo.chat.ChatRelationshipVo;
import cn.hjf.job.model.vo.interview.ResumeDeliveryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 简历投递记录表 服务实现类
 * </p>
 *
 * @author hjf
 * @since 2025-02-04
 */
@Service
public class ResumeDeliveryServiceImpl extends ServiceImpl<ResumeDeliveryMapper, ResumeDelivery> implements ResumeDeliveryService {

    @Resource
    private ResumeDeliveryMapper resumeDeliveryMapper;

    @Override
    public ResumeDeliveryVo candidateResumeDelivery(Long resumeId, ChatRelationshipVo chatRelationshipVo) {
        try {
            // 构建 简历投递记录
            ResumeDelivery resumeDelivery = new ResumeDelivery();
            resumeDelivery.setRecruiterId(chatRelationshipVo.getRecruiterId());
            resumeDelivery.setCandidateId(chatRelationshipVo.getCandidateId());
            resumeDelivery.setPositionId(chatRelationshipVo.getPositionId());
            resumeDelivery.setResumeId(resumeId);
            resumeDelivery.setStatus(0);

            int insert = resumeDeliveryMapper.insert(resumeDelivery);
            if (insert != 1) {
                return null;
            }
            return convertToVo(resumeDelivery);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ResumeDeliveryVo getRecruiterIdResumeDeliveryVoByCandidateIdAndPositionId(Long recruiterId, Long candidateId, Long positionId) {
        try {
            LambdaQueryWrapper<ResumeDelivery> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper.eq(ResumeDelivery::getRecruiterId, recruiterId)
                    .eq(ResumeDelivery::getCandidateId, candidateId)
                    .eq(ResumeDelivery::getPositionId, positionId);

            ResumeDelivery resumeDelivery = resumeDeliveryMapper.selectOne(queryWrapper);

            return convertToVo(resumeDelivery);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ResumeDeliveryVo getCandidateIdResumeDeliveryVoByRecruiterIdAndPositionId(Long candidateId, Long recruiterId, Long positionId) {
        try {
            LambdaQueryWrapper<ResumeDelivery> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper.eq(ResumeDelivery::getRecruiterId, recruiterId)
                    .eq(ResumeDelivery::getCandidateId, candidateId)
                    .eq(ResumeDelivery::getPositionId, positionId);

            ResumeDelivery resumeDelivery = resumeDeliveryMapper.selectOne(queryWrapper);

            return convertToVo(resumeDelivery);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ResumeDeliveryVo getResumeDeliveryVoByResumeId(Long userId, Long positionId, Long resumeId) {
        try {
            LambdaQueryWrapper<ResumeDelivery> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper.eq(ResumeDelivery::getResumeId, resumeId)
                    .eq(ResumeDelivery::getPositionId, positionId)
                    .eq(ResumeDelivery::getRecruiterId, userId);

            ResumeDelivery resumeDelivery = resumeDeliveryMapper.selectOne(queryWrapper);

            return convertToVo(resumeDelivery);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public PageVo<ResumeDeliveryVo> getResumeDeliveryVoPageByUserId(Page<ResumeDelivery> resumeDeliveryPage, Long userId) {
        try {
            LambdaQueryWrapper<ResumeDelivery> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper.eq(ResumeDelivery::getRecruiterId, userId);

            Page<ResumeDelivery> pageResult = resumeDeliveryMapper.selectPage(resumeDeliveryPage, queryWrapper);

            PageVo<ResumeDeliveryVo> pageVo = new PageVo<>();
            pageVo.setTotal(pageResult.getTotal());
            pageVo.setPage(pageResult.getCurrent());
            pageVo.setPages(pageResult.getPages());
            pageVo.setLimit(pageResult.getSize());
            pageVo.setRecords(pageResult.getRecords().stream().map(this::convertToVo).toList());

            return pageVo;
        } catch (Exception e) {
            return null;
        }
    }

    private ResumeDeliveryVo convertToVo(ResumeDelivery resumeDelivery) {
        ResumeDeliveryVo resumeDeliveryVo = new ResumeDeliveryVo();
        BeanUtils.copyProperties(resumeDelivery, resumeDeliveryVo);
        return resumeDeliveryVo;
    }
}
