package cn.hjf.job.company.service.impl;

import cn.hjf.job.company.mapper.CompanyEmployeeMapper;
import cn.hjf.job.company.service.CompanyEmployeeService;
import cn.hjf.job.model.entity.company.CompanyEmployee;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class CompanyEmployeeServiceImpl extends ServiceImpl<CompanyEmployeeMapper, CompanyEmployee> implements CompanyEmployeeService {

    @Resource
    private CompanyEmployeeMapper companyEmployeeMapper;

    @Override
    public boolean setCompanyAdminEmployee(Long companyId, Long userId, Long titleId) {
        CompanyEmployee companyEmployee = new CompanyEmployee(userId, companyId, titleId, 1);
        int insert = companyEmployeeMapper.insert(companyEmployee);
        return insert == 1;
    }
}
