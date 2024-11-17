package cn.hjf.job.company.service.impl;

import cn.hjf.job.common.constant.RedisConstant;
import cn.hjf.job.common.rabbit.service.RabbitService;
import cn.hjf.job.company.mapper.CompanyIndustryMapper;
import cn.hjf.job.company.service.CompanyIndustryService;
import cn.hjf.job.model.entity.company.CompanyIndustry;
import cn.hjf.job.model.vo.base.PageVo;
import cn.hjf.job.model.vo.company.IndustryVo;
import cn.hjf.job.model.vo.company.SubIndustriesVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
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
public class CompanyIndustryServiceImpl extends ServiceImpl<CompanyIndustryMapper, CompanyIndustry> implements CompanyIndustryService {

    @Resource
    private CompanyIndustryMapper companyIndustryMapper;

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, IndustryVo> industryVosRedisTemplate;

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, SubIndustriesVo> subIndustriesVoRedisTemplate;

    @Resource
    private RabbitService rabbitService;


    @Override
    public PageVo<IndustryVo> selectIndustryByPage(Page<CompanyIndustry> industryPage) {
        long start = (industryPage.getCurrent() - 1) * industryPage.getSize();
        long end = start + industryPage.getSize() - 1; // 计算分页的起始和结束位置
        // 从 Redis 查询分页数据
        Set<IndustryVo> industryVos = industryVosRedisTemplate.opsForZSet().range(RedisConstant.COMPANY_INDUSTRY_PARENT, start, end);
        // 如果查询到父行业数据
        if (industryVos != null && !industryVos.isEmpty()) {
            // 查询到数据后，根据父行业ID查询其子行业，并组合返回结果
            List<IndustryVo> voList = industryVos.stream().toList();
            List<IndustryVo> result = getIndustriesWithSubIndustries(voList);
            // 计算总数量和总页数
            long totalCount = industryVosRedisTemplate.opsForZSet().size(RedisConstant.COMPANY_INDUSTRY_PARENT);
            long totalPages = (totalCount + industryPage.getSize() - 1) / industryPage.getSize(); // 计算总页数

            return new PageVo<>(result, totalPages, totalCount);
        }

        // 如果没有命中 Redis，则查询数据库中的所有父行业数据
        LambdaQueryWrapper<CompanyIndustry> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.isNull(CompanyIndustry::getParentId);  // 只查询父行业

        // 获取所有父行业数据
        List<CompanyIndustry> companyIndustries = companyIndustryMapper.selectList(queryWrapper);

        // 转为vo
        List<IndustryVo> industryVosToRedis = new ArrayList<>();
        for (CompanyIndustry companyIndustry : companyIndustries) {
            IndustryVo industryVo = new IndustryVo();
            BeanUtils.copyProperties(companyIndustry, industryVo);
            industryVosToRedis.add(industryVo);
        }

        // 手动分页
        int startIndex = (int) ((industryPage.getCurrent() - 1) * industryPage.getSize());
        int endIndex = (int) Math.min(startIndex + industryPage.getSize(), companyIndustries.size());

        // 分页
        List<IndustryVo> industryVosToResult = new ArrayList<>();
        for (int i = startIndex; i < endIndex; i++) {
            IndustryVo industryVo = industryVosToRedis.get(i);
            industryVosToResult.add(industryVo);
        }

        // 初始化 GenericJackson2JsonRedisSerializer
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();

        // 使用 executePipelined 批量写入数据
        industryVosRedisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (IndustryVo industryVo : industryVosToRedis) {
                // 将 IndustryVo 对象序列化为 JSON 字节数组
                byte[] serializedData = jsonSerializer.serialize(industryVo);

                // 使用 zAdd 将序列化后的数据添加到 ZSet 中
                connection.zAdd(RedisConstant.COMPANY_INDUSTRY_PARENT.getBytes(),
                        industryVo.getId(),
                        serializedData);  // 使用 JSON 字节数组作为成员
            }
            return null;
        });

        // 设置缓存过期时间（24小时）
        industryVosRedisTemplate.expire(RedisConstant.COMPANY_INDUSTRY_PARENT,
                RedisConstant.PUBLIC_COMPANY_INDUSTRY_TIME_OUT, TimeUnit.SECONDS);

        // 查询子行业
        List<IndustryVo> result = getIndustriesWithSubIndustries(industryVosToResult);

        // 计算总数量和总页数
        long totalCount = companyIndustries.size();
        long totalPages = (totalCount + industryPage.getSize() - 1) / industryPage.getSize(); // 计算总页数

        // 返回分页结果
        return new PageVo<>(result, totalPages, totalCount);
    }

    @Override
    public List<SubIndustriesVo> getSubIndustriesFromRedisOrDb(Long parentId) {
        // 从 Redis 查询子行业数据
        List<SubIndustriesVo> subIndustriesVos = subIndustriesVoRedisTemplate.opsForList().range(RedisConstant.COMPANY_SUB_INDUSTRIES + parentId, 0, -1);

        if (subIndustriesVos == null || subIndustriesVos.isEmpty()) {
            // 如果没有命中缓存，则查询数据库并更新缓存
            LambdaQueryWrapper<CompanyIndustry> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(CompanyIndustry::getParentId, parentId);
            List<CompanyIndustry> subIndustries = companyIndustryMapper.selectList(queryWrapper);

            subIndustriesVos = subIndustries.stream()
                    .map(subIndustry -> {
                        SubIndustriesVo subIndustriesVo = new SubIndustriesVo();
                        BeanUtils.copyProperties(subIndustry, subIndustriesVo);
                        return subIndustriesVo;
                    })
                    .collect(Collectors.toList());

            // 将查询到的子行业存储到 Redis 中
            subIndustriesVoRedisTemplate.opsForList().rightPushAll(RedisConstant.COMPANY_SUB_INDUSTRIES + parentId, subIndustriesVos);

            // 设置缓存过期时间（如 1 小时）
            subIndustriesVoRedisTemplate.expire(RedisConstant.COMPANY_SUB_INDUSTRIES + parentId, RedisConstant.PUBLIC_COMPANY_INDUSTRY_TIME_OUT, TimeUnit.SECONDS);
        }

        return subIndustriesVos;
    }


    // 封装查询父行业及其子行业
    private List<IndustryVo> getIndustriesWithSubIndustries(List<IndustryVo> industryVos) {
        List<IndustryVo> result = new ArrayList<>();
        for (IndustryVo industryVo : industryVos) {
            // 查询父行业的子行业
            List<SubIndustriesVo> subIndustriesVos = getSubIndustriesFromRedisOrDb(industryVo.getId());

            // 设置子行业
            industryVo.setSubIndustriesVo(subIndustriesVos);

            // 将行业及其子行业封装到结果列表
            result.add(industryVo);
        }
        return result;
    }

}
