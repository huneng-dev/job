package cn.hjf.job.position.service;

import cn.hjf.job.model.entity.position.PositionType;
import cn.hjf.job.model.vo.position.PositionTypeVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hjf
 * @since 2024-10-25
 */
public interface PositionTypeService extends IService<PositionType> {


    /**
     * 根据行业id查询其下的职位信息
     *
     * @param ids 行业id
     * @return Map<Long, List < PositionTypeVo>>
     */
    public Map<Long, List<PositionTypeVo>> queryPositionTypeByIndustryId(List<Long> ids);

    /**
     * 根据职位类型 id,查询职位类型描述
     *
     * @param id 职位 id
     * @return 职位 type
     */
    String getPositionTypeDescByPositionId(Long id);
}
