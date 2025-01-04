package cn.hjf.job.company.service;

import cn.hjf.job.model.dto.company.CompanyIdAndNameDTO;
import cn.hjf.job.model.entity.company.CompanyInfo;
import cn.hjf.job.model.dto.company.CompanyInfoQuery;
import cn.hjf.job.model.form.company.CompanyInfoAndBusinessLicenseForm;
import cn.hjf.job.model.vo.company.CompanyInfoCandidateVo;
import cn.hjf.job.model.vo.company.CompanyInfoEsVo;
import cn.hjf.job.model.vo.company.CompanyInfoRecruiterVo;
import cn.hjf.job.model.vo.company.CompanyInfoVo;
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
public interface CompanyInfoService extends IService<CompanyInfo> {


    /**
     * 根据用户 id 查询所在的公司信息
     *
     * @param userId 用户id
     * @return CompanyInfoQuery
     */
    CompanyInfoQuery findCompanyInfoByUserId(Long userId);

    /**
     * 获取公司名列表
     *
     * @param name 公司名
     * @return List<CompanyIdAndNameDTO>
     */
    List<CompanyIdAndNameDTO> findCompanyIndexAndNameByName(String name);

    /**
     * 保存公司信息和营业执照信息(注册公司流程)
     *
     * @param companyInfoAndBusinessLicenseForm 公司信息和营业执照表单
     */
    void saveCompanyInfoAndBusinessLicense(CompanyInfoAndBusinessLicenseForm companyInfoAndBusinessLicenseForm, Long userId);

    /**
     * 设置公司的状态
     *
     * @param companyId 公司 id
     * @param status    状态
     * @return 是否成功
     */
    boolean setCompanyStatus(Long companyId, Integer status);

    /**
     * 获取公司信息 (ES 存储)
     *
     * @param id 公司 id
     * @return CompanyInfoEsVo
     */
    CompanyInfoEsVo getCompanyInfoEsById(Long id);

    /**
     * 获取公司信息
     *
     * @param id 公司 id
     * @return CompanyInfoVo
     */
    CompanyInfoVo getCompanyInfoById(Long id);

    /**
     * 获取招聘端公司详情
     *
     * @param id 用户 id
     * @return CompanyInfoRecruiterVo
     */
    CompanyInfoRecruiterVo getCompanyInfoRecruiterVo(Long id);

    /**
     * 获取应聘端公司详情
     *
     * @param companyId 公司 id
     * @return CompanyInfoCandidateVo
     */
    CompanyInfoCandidateVo getCompanyInfoCandidateVo(Long companyId);

}
