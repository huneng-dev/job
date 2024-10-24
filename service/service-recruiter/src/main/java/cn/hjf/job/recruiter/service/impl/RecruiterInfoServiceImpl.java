package cn.hjf.job.recruiter.service.impl;

import cn.hjf.job.model.entity.recruiter.RecruiterInfo;
import cn.hjf.job.recruiter.mapper.RecruiterInfoMapper;
import cn.hjf.job.recruiter.service.RecruiterInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hjf
 * @since 2024-10-25
 */
@Service
public class RecruiterInfoServiceImpl extends ServiceImpl<RecruiterInfoMapper, RecruiterInfo> implements RecruiterInfoService {

    @Resource
    private RecruiterInfoMapper recruiterInfoMapper;

    @Override
    public RecruiterInfo getRecruiterInfoById(Integer id) {
        return recruiterInfoMapper.selectById(id);
    }
}
