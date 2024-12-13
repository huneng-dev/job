package cn.hjf.job.company.service.impl;

import cn.hjf.job.company.mapper.CompanyTitleMapper;
import cn.hjf.job.company.service.CompanyTitleService;
import cn.hjf.job.model.entity.company.CompanyTitle;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class CompanyTitleServiceImpl extends ServiceImpl<CompanyTitleMapper, CompanyTitle> implements CompanyTitleService {

    @Resource
    private CompanyTitleMapper companyTitleMapper;

    @Override
    public Long setCompanyAdminTitle(Long companyId) {
        CompanyTitle companyTitle = new CompanyTitle(companyId, "管理员");
        int insert = companyTitleMapper.insert(companyTitle);
        if (insert != 1) throw new RuntimeException();
        return companyTitle.getId();
    }

    @Override
    public String findTitleNameById(Long titleId) {
        LambdaQueryWrapper<CompanyTitle> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(CompanyTitle::getTitleName)
                .eq(CompanyTitle::getId, titleId);

        CompanyTitle companyTitle = companyTitleMapper.selectOne(lambdaQueryWrapper);

        return companyTitle.getTitleName();
    }


}
