package cn.hjf.job.company.service.impl;

import cn.hjf.job.company.mapper.CompanySizeMapper;
import cn.hjf.job.company.service.CompanySizeService;
import cn.hjf.job.model.entity.company.CompanySize;
import cn.hjf.job.model.vo.company.CompanySizeVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hjf
 * @since 2024-10-31
 */
@Service
public class CompanySizeServiceImpl extends ServiceImpl<CompanySizeMapper, CompanySize> implements CompanySizeService {

    @Resource
    private CompanySizeMapper companySizeMapper;

    @Override
    public List<CompanySizeVo> findCompanySizeAll() {
        LambdaQueryWrapper<CompanySize> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .select(CompanySize::getId, CompanySize::getSizeDescription);

        List<CompanySize> companySizes = companySizeMapper.selectList(queryWrapper);

        return companySizes.stream().map(
                companySize -> new CompanySizeVo(Math.toIntExact(companySize.getId()), companySize.getSizeDescription())
        ).toList();
    }
}
