package cn.hjf.job.company.service.impl;

import cn.hjf.job.company.mapper.CompanyInfoMapper;
import cn.hjf.job.company.service.CompanyInfoService;
import cn.hjf.job.model.entity.company.CompanyInfo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hjf
 * @since 2024-10-31
 */
@Service
public class CompanyInfoServiceImpl extends ServiceImpl<CompanyInfoMapper, CompanyInfo> implements CompanyInfoService {

    @Resource
    private CompanyInfoMapper companyInfoMapper;

    @Override
    public CompanyInfo getCompanyInfoById(Integer id) {
        return companyInfoMapper.selectById(id);
    }
}
