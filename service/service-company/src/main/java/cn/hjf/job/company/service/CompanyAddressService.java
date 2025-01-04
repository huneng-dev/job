package cn.hjf.job.company.service;

import cn.hjf.job.model.entity.company.CompanyAddress;
import cn.hjf.job.model.form.company.AddressInfoForm;
import cn.hjf.job.model.vo.company.AddressInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hjf
 * @since 2024-10-31
 */
public interface CompanyAddressService extends IService<CompanyAddress> {

    boolean add(AddressInfoForm addressInfoForm, Long userId);

    List<AddressInfoVo> findAllCompanyAddressByUserId(Long userId);

    boolean deleteCompanyAddressById(Long addressId, Long userId);

    AddressInfoVo getAddressById(Long addressId);

    List<AddressInfoVo> getAddresses(Long companyId);

}
