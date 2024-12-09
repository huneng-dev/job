package cn.hjf.job.company.receiver;

import cn.hjf.job.company.mapper.CompanyIndustryMapper;
import cn.hjf.job.model.vo.company.IndustryVo;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompanyIndustryReceiver {

    @Resource
    private CompanyIndustryMapper companyIndustryMapper;

    @Resource
    private RedisTemplate<String, List<IndustryVo>> redisTemplate;


}
