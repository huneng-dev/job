package cn.hjf.job.candidate.service;

import cn.hjf.job.model.entity.candidate.CandidateInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hjf
 * @since 2024-10-24
 */
public interface CandidateInfoService extends IService<CandidateInfo> {

    public CandidateInfo getCandidateInfo(Integer id);

}
