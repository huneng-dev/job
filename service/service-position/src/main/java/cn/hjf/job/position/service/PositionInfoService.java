package cn.hjf.job.position.service;

import cn.hjf.job.model.entity.position.PositionInfo;
import cn.hjf.job.model.form.position.PositionInfoForm;
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

    boolean create(PositionInfoForm positionInfoForm, Long userId);

}
