package cn.hjf.job.company.service.impl;

import cn.hjf.job.company.mapper.CompanyAddressMapper;
import cn.hjf.job.company.service.CompanyAddressService;
import cn.hjf.job.company.service.CompanyEmployeeService;
import cn.hjf.job.model.entity.base.BaseEntity;
import cn.hjf.job.model.entity.company.CompanyAddress;
import cn.hjf.job.model.form.company.AddressInfoForm;
import cn.hjf.job.model.vo.company.AddressInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hjf
 * @since 2024-10-31
 */
@Service
public class CompanyAddressServiceImpl extends ServiceImpl<CompanyAddressMapper, CompanyAddress> implements CompanyAddressService {

    @Resource(name = "companyEmployeeServiceImpl")
    private CompanyEmployeeService companyEmployeeService;

    @Resource
    private CompanyAddressMapper companyAddressMapper;

    @Override
    public boolean add(AddressInfoForm addressInfoForm, Long userId) {
        // 通过用户 id 获取公司 id
        Long companyId = companyEmployeeService.findCompanyIdByUserId(userId);
        if (companyId == null) return false;

        // 每个公司只能插入 10 个地址
        LambdaQueryWrapper<CompanyAddress> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CompanyAddress::getCompanyId, companyId);
        Long count = companyAddressMapper.selectCount(queryWrapper);
        if (count >= 10) throw new RuntimeException("只能保存10个地址");
        // 插入 地址到数据库
        CompanyAddress companyAddress = new CompanyAddress();
        BeanUtils.copyProperties(addressInfoForm, companyAddress);
        companyAddress.setCompanyId(companyId);
        companyAddressMapper.insert(companyAddress);

        return true;
    }

    @Override
    public List<AddressInfoVo> findAllCompanyAddressByUserId(Long userId) {
        // 通过用户 id 获取公司 id
        Long companyId = companyEmployeeService.findCompanyIdByUserId(userId);
        if (companyId == null) throw new RuntimeException();

        LambdaQueryWrapper<CompanyAddress> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(
                CompanyAddress::getId,
                CompanyAddress::getProvince,
                CompanyAddress::getCity,
                CompanyAddress::getDistrict,
                CompanyAddress::getAddress,
                CompanyAddress::getLongitude,
                CompanyAddress::getLatitude
        ).eq(CompanyAddress::getCompanyId, companyId).orderByDesc(CompanyAddress::getId);

        List<CompanyAddress> companyAddresses = companyAddressMapper.selectList(queryWrapper);
        if (companyAddresses == null) throw new RuntimeException();

        return companyAddresses.stream().map(
                companyAddress -> new AddressInfoVo(
                        companyAddress.getId(),
                        companyAddress.getProvince(),
                        companyAddress.getCity(),
                        companyAddress.getDistrict(),
                        companyAddress.getAddress(),
                        companyAddress.getLongitude(),
                        companyAddress.getLatitude()
                )
        ).toList();
    }

    @Override
    public boolean deleteCompanyAddressById(Long addressId, Long userId) {
        // 查询当前用户的公司 id
        Long companyId = companyEmployeeService.findCompanyIdByUserId(userId);

        // 删除地址
        LambdaQueryWrapper<CompanyAddress> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CompanyAddress::getId, addressId).eq(CompanyAddress::getCompanyId, companyId);
        int delete = companyAddressMapper.delete(queryWrapper);
        return delete == 1;
    }

    @Override
    public AddressInfoVo getAddressById(Long addressId) {
        // 获取地址
        CompanyAddress companyAddress = companyAddressMapper.selectById(addressId);
        AddressInfoVo addressInfoVo = new AddressInfoVo();
        BeanUtils.copyProperties(companyAddress, addressInfoVo);
        return addressInfoVo;
    }
}
