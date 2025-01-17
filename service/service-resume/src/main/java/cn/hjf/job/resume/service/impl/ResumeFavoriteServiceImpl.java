package cn.hjf.job.resume.service.impl;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.entity.resume.ResumeFavorite;
import cn.hjf.job.model.vo.base.PageVo;
import cn.hjf.job.model.vo.resume.EducationBackgroundVo;
import cn.hjf.job.model.vo.resume.JobExpectationVo;
import cn.hjf.job.model.vo.resume.ResumeFavoriteInfoVo;
import cn.hjf.job.model.vo.resume.ResumeVo;
import cn.hjf.job.model.vo.user.UserInfoAllVo;
import cn.hjf.job.resume.config.KeyProperties;
import cn.hjf.job.resume.mapper.ResumeFavoriteMapper;
import cn.hjf.job.resume.service.EducationBackgroundService;
import cn.hjf.job.resume.service.JobExpectationService;
import cn.hjf.job.resume.service.ResumeFavoriteService;
import cn.hjf.job.resume.service.ResumeInfoService;
import cn.hjf.job.user.client.UserInfoFeignClient;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

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

    @Resource
    private JobExpectationService jobExpectationService;

    @Resource
    private EducationBackgroundService educationBackgroundService;

    @Resource
    private UserInfoFeignClient userInfoFeignClient;

    @Resource
    private KeyProperties keyProperties;

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

    @Override
    public PageVo<ResumeFavoriteInfoVo> findResumeFavoritePage(Page<ResumeFavorite> resumeFavoritePage, Long userId) {
        // 1. 查询当前用户的收藏的简历id
        LambdaQueryWrapper<ResumeFavorite> resumeFavoriteLambdaQueryWrapper = new LambdaQueryWrapper<>();
        resumeFavoriteLambdaQueryWrapper.eq(ResumeFavorite::getRecruiterId, userId);

        Page<ResumeFavorite> selectPage = resumeFavoriteMapper.selectPage(resumeFavoritePage, resumeFavoriteLambdaQueryWrapper);

        PageVo<ResumeFavoriteInfoVo> resumeFavoriteInfoVoPageVo = new PageVo<>();

        // 设置分页信息
        resumeFavoriteInfoVoPageVo.setTotal(selectPage.getTotal());
        resumeFavoriteInfoVoPageVo.setPage(selectPage.getCurrent());
        resumeFavoriteInfoVoPageVo.setPages(selectPage.getPages());
        resumeFavoriteInfoVoPageVo.setLimit(selectPage.getSize());

        // 获取查询记录
        List<ResumeFavorite> records = selectPage.getRecords();

        if (records == null || records.isEmpty()) {
            return resumeFavoriteInfoVoPageVo;
        }

        // 简历 ids
        List<Long> resumeIds = new ArrayList<>(records.size());

        List<ResumeFavoriteInfoVo> resumeFavoriteInfoVos = records.stream().map(resumeFavorite -> {
            ResumeFavoriteInfoVo resumeFavoriteInfoVo = new ResumeFavoriteInfoVo();
            // 设置 简历id、操作时间
            resumeFavoriteInfoVo.setId(resumeFavorite.getResumeId());
            resumeFavoriteInfoVo.setUpdateTime(resumeFavorite.getUpdateTime());
            // 记录简历 ids
            resumeIds.add(resumeFavorite.getResumeId());
            return resumeFavoriteInfoVo;
        }).toList();

        // 2. 获取简历们的信息（异步获取）
        CompletableFuture<Map<Long, ResumeVo>> resumeVosAsync = resumeInfoService.getResumeVosAsync(new ArrayList<>(resumeIds));
        CompletableFuture<Map<Long, JobExpectationVo>> jobExpectationVosAsync = jobExpectationService.getJobExpectationVosAsync(new ArrayList<>(resumeIds));
        CompletableFuture<Map<Long, EducationBackgroundVo>> educationBackgroundVoAsync = educationBackgroundService.getEducationBackgroundVoAsync(new ArrayList<>(resumeIds));

        // 3. 同步等待所有异步操作完成，并拼接数据
        try {
            // 获取所有的异步数据
            Map<Long, ResumeVo> resumeVoMap = resumeVosAsync.get();
            Map<Long, JobExpectationVo> jobExpectationVoMap = jobExpectationVosAsync.get();
            Map<Long, EducationBackgroundVo> educationBackgroundVoMap = educationBackgroundVoAsync.get();

            HashSet<Long> userIds = new HashSet<>();

            // 拼接返回数据
            for (ResumeFavoriteInfoVo resumeFavoriteInfoVo : resumeFavoriteInfoVos) {
                // 拼接 简历数据
                ResumeVo resumeVo = resumeVoMap.get(resumeFavoriteInfoVo.getId());
                if (resumeVo != null) {
                    resumeFavoriteInfoVo.setCandidateId(resumeVo.getCandidateId());
                    resumeFavoriteInfoVo.setJobStatus(resumeVo.getJobStatus());
                    resumeFavoriteInfoVo.setPersonalAdvantages(resumeVo.getPersonalAdvantages());
                    resumeFavoriteInfoVo.setProfessionalSkills(resumeVo.getProfessionalSkills());
                    userIds.add(resumeVo.getCandidateId());
                }

                JobExpectationVo jobExpectationVo = jobExpectationVoMap.get(resumeFavoriteInfoVo.getId());
                if (jobExpectationVo != null) {
                    resumeFavoriteInfoVo.setExpectedPositionId(jobExpectationVo.getExpectedPositionId());
                    resumeFavoriteInfoVo.setIndustryId(jobExpectationVo.getIndustryId());
                    resumeFavoriteInfoVo.setWorkCity(jobExpectationVo.getWorkCity());
                    resumeFavoriteInfoVo.setSalaryMin(jobExpectationVo.getSalaryMin());
                    resumeFavoriteInfoVo.setSalaryMax(jobExpectationVo.getSalaryMax());
                    resumeFavoriteInfoVo.setIsNegotiable(jobExpectationVo.getIsNegotiable());
                    resumeFavoriteInfoVo.setJobType(jobExpectationVo.getJobType());
                }

                EducationBackgroundVo educationBackgroundVo = educationBackgroundVoMap.get(resumeFavoriteInfoVo.getId());
                if (educationBackgroundVo != null) {
                    resumeFavoriteInfoVo.setSchoolName(educationBackgroundVo.getSchoolName());
                    resumeFavoriteInfoVo.setMajor(educationBackgroundVo.getMajor());
                    resumeFavoriteInfoVo.setEducationLevel(educationBackgroundVo.getEducationLevel());
                    resumeFavoriteInfoVo.setStartYear(educationBackgroundVo.getStartYear());
                    resumeFavoriteInfoVo.setEndYear(educationBackgroundVo.getEndYear());
                }
            }

            ArrayList<Long> userIdList = new ArrayList<>(userIds);

            // 查询简历对应的用户信息
            Result<Map<Long, UserInfoAllVo>> userInfoAllVos = userInfoFeignClient.getUserInfoAllVos(userIdList, keyProperties.getKey());
            if (Objects.equals(userInfoAllVos.getCode(), 200)) {
                Map<Long, UserInfoAllVo> userInfoAllVosData = userInfoAllVos.getData();
                for (ResumeFavoriteInfoVo resumeFavoriteInfoVo : resumeFavoriteInfoVos) {
                    UserInfoAllVo userInfoAllVo = userInfoAllVosData.get(resumeFavoriteInfoVo.getCandidateId());
                    resumeFavoriteInfoVo.setSurname(String.valueOf(userInfoAllVo.getName().charAt(0)));
                }
            } else {
                return null;
            }

            // 设置最终的结果
            resumeFavoriteInfoVoPageVo.setRecords(resumeFavoriteInfoVos);
        } catch (Exception e) {
            return null;
        }

        // 返回处理后的数据
        return resumeFavoriteInfoVoPageVo;
    }

    @Override
    public Boolean deleteResumeFavoritesByResumeId(Long resumeId) {
        LambdaQueryWrapper<ResumeFavorite> resumeFavoriteLambdaQueryWrapper = new LambdaQueryWrapper<>();
        resumeFavoriteLambdaQueryWrapper.eq(ResumeFavorite::getResumeId, resumeId);
        resumeFavoriteMapper.delete(resumeFavoriteLambdaQueryWrapper);
        return true;
    }
}
