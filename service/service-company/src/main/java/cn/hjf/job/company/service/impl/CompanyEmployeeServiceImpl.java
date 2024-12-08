package cn.hjf.job.company.service.impl;

import cn.hjf.job.company.mapper.CompanyEmployeeMapper;
import cn.hjf.job.company.service.CompanyEmployeeService;
import cn.hjf.job.model.entity.company.CompanyEmployee;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CompanyEmployeeServiceImpl extends ServiceImpl<CompanyEmployeeMapper, CompanyEmployee> implements CompanyEmployeeService {
}
