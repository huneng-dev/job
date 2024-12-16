package cn.hjf.job.position.service;

import cn.hjf.job.model.entity.position.PositionInfo;
import cn.hjf.job.model.form.position.PositionInfoForm;
import cn.hjf.job.model.vo.base.PageVo;
import cn.hjf.job.model.vo.position.RecruiterBasePositionInfoVo;
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
    PageVo<RecruiterBasePositionInfoVo> findRecruiterBasePositionInfoByUserId(
            Page<PositionInfo> positionInfoPage,
            String positionName,
            Integer status,
            Long userId
    );

}
