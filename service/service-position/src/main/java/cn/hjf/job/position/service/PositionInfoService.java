package cn.hjf.job.position.service;

import cn.hjf.job.model.entity.position.PositionInfo;
import cn.hjf.job.model.form.position.PositionInfoForm;
import cn.hjf.job.model.request.position.CandidatePositionPageParam;
import cn.hjf.job.model.vo.base.PagePositionEsVo;
import cn.hjf.job.model.vo.base.PageVo;
import cn.hjf.job.model.vo.position.CandidateBasePositionInfoVo;
import cn.hjf.job.model.vo.position.CandidatePositionInfoVo;
import cn.hjf.job.model.vo.position.RecruiterBasePositionInfoVo;
import cn.hjf.job.model.vo.position.RecruiterPositionInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hjf
 * @since 2024-10-31
 */
public interface PositionInfoService extends IService<PositionInfo> {

    /**
     * 创建职位信息
     *
     * @param positionInfoForm 职位表单
     * @param userId           用户 id
     * @return 是否成功
     */
    boolean create(PositionInfoForm positionInfoForm, Long userId);


    /**
     * 查询 职位列表
     *
     * @param positionName 模糊搜索关键词
     * @param status       状态
     * @param userId       用户 id
     * @return PageVo<RecruiterBasePositionInfoVo>
     */
    PageVo<RecruiterBasePositionInfoVo> findRecruiterBasePositionInfoByUserId(Page<PositionInfo> positionInfoPage, String positionName, Integer status, Long userId);

    /**
     * 根据职位id和用户id获取职位详情
     *
     * @param positionId 职位id
     * @param userId     用户id
     * @return RecruiterPositionInfoVo
     */
    RecruiterPositionInfoVo getRecruiterPositionInfoVoById(Long positionId, Long userId);

    /**
     * 设置职位状态为 “开放中”
     *
     * @param positionId 职位 id
     * @param userId     用户 id
     * @return 是否成功
     */
    boolean setPositionStatusToOpen(Long positionId, Long userId);

    /**
     * 设置职位状为 “待开放”
     *
     * @param positionId 职位 id
     * @param userId     用户 id
     * @return 是否成功
     */
    boolean setPositionStatusToNoOpen(Long positionId, Long userId);

    /**
     * 设置职位为已关闭
     *
     * @param positionId 职位 id
     * @param userId     用户 id
     * @return 是否成功
     */
    boolean setPositionStatusToClose(Long positionId, Long userId);

    /**
     * 删除职位
     *
     * @param positionId 职位 id
     * @param userId     用户 id
     * @return 受否成功
     */
    boolean deletePositionById(Long positionId, Long userId);

    /**
     * 搜索应聘端基本职业信息
     *
     * @return PagePositionEsVo<CandidateBasePositionInfoVo>
     */
    PagePositionEsVo<CandidateBasePositionInfoVo> searchCandidateBasePositionInfo(Integer limit, CandidatePositionPageParam candidatePositionPageParam);


    /**
     * 根据职位 id 获取职位详情
     * 1.职位信息 2. 公司信息 3.职位负责人信息 4.地址信息
     *
     * @param id 职位 id
     * @return CandidatePositionInfoVo
     */
    CandidatePositionInfoVo getCandidatePositionInfoById(Long id);


    /**
     * 获取职位数量
     *
     * @param companyId 公司 id
     * @param status    职位状态,如果为 null 获取全部职位
     * @return Long
     */
    Long getCompanyPositionCount(Long companyId, Integer status);
}
