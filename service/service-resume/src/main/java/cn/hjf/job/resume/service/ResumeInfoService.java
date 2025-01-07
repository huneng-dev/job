package cn.hjf.job.resume.service;

import cn.hjf.job.model.entity.resume.ResumeInfo;
import cn.hjf.job.model.form.resume.BaseResumeForm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hjf
 * @since 2025-01-07
 */
public interface ResumeInfoService extends IService<ResumeInfo> {

    /**
     * 创建基础简历
     *
     * @param baseResumeForm 基础简历表单
     * @param userId         用户 id
     * @return 简历 id
     */
    Long createBaseResume(BaseResumeForm baseResumeForm, Long userId);


}
