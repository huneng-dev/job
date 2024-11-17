package cn.hjf.job.position.service.impl;

import cn.hjf.job.common.constant.RedisConstant;
import cn.hjf.job.model.entity.position.PositionType;
import cn.hjf.job.model.vo.position.PositionTypeVo;
import cn.hjf.job.position.mapper.PositionTypeMapper;
import cn.hjf.job.position.service.PositionTypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hjf
 * @since 2024-10-25
 */
@Service
public class PositionTypeServiceImpl extends ServiceImpl<PositionTypeMapper, PositionType> implements PositionTypeService {

    @Resource
    private PositionTypeMapper positionTypeMapper;

    @Resource
    private RedisTemplate<String, List<PositionTypeVo>> redisTemplate;


    @Override
    public Map<Long, List<PositionTypeVo>> queryPositionTypeByIndustryId(List<Long> ids) {

        List<Object> redisResults = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            RedisStringCommands stringCommands = connection.stringCommands();
            for (Long key : ids) {
                String redisKey = RedisConstant.INDUSTRY_POSITION_TYPE_S + key;
                connection.listCommands().lRange(redisKey.getBytes(), 0, -1); // 获取整个列表
            }
            return null;
        });

        Map<Long, List<PositionTypeVo>> resultMap = new HashMap<>();
        List<Long> missingIds = new ArrayList<>();

        for (int i = 0; i < ids.size(); i++) {

            Long id = ids.get(i);

            @SuppressWarnings("unchecked")
            List<Object> item = (List<Object>) redisResults.get(i);

            if (item != null && !item.isEmpty()) {

                @SuppressWarnings("unchecked")
                List<PositionTypeVo> positionTypeVos = (List<PositionTypeVo>) item.get(0);
                if (!positionTypeVos.isEmpty() && id.equals(positionTypeVos.get(0).getIndustryId())) {
                    resultMap.put(id, positionTypeVos);
                } else {
                    missingIds.add(id);
                }
            } else {
                missingIds.add(id);
            }
        }

        if (!missingIds.isEmpty()) {
            // 获取没有命中的职位
            Map<Long, List<PositionTypeVo>> positionTypeListMap = queryPositionTypeFromDatabase(missingIds);

            resultMap.putAll(positionTypeListMap);

            // 将查询结果存入 Redis Hash 中
            for (Map.Entry<Long, List<PositionTypeVo>> entry : positionTypeListMap.entrySet()) {
                redisTemplate.opsForList().rightPush(
                        RedisConstant.INDUSTRY_POSITION_TYPE_S + entry.getKey(),
                        entry.getValue()
                );
                redisTemplate.expire(
                        RedisConstant.INDUSTRY_POSITION_TYPE_S + entry.getKey(),
                        RedisConstant.PUBLIC_COMPANY_INDUSTRY_TIME_OUT,
                        TimeUnit.SECONDS
                );
            }
        }

        // 4. 返回结果
        return resultMap;
    }

    // 从数据库中查询未命中的数据
    private Map<Long, List<PositionTypeVo>> queryPositionTypeFromDatabase(List<Long> missingIds) {
        // 查询没有命中的职位列表
        LambdaQueryWrapper<PositionType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PositionType::getIndustryId, missingIds);
        List<PositionType> positionTypes = positionTypeMapper.selectList(queryWrapper);

        // 组装响应结果
        return positionTypes.stream()
                .map(positionType -> {
                    // 转换为 VO 对象
                    PositionTypeVo positionTypeVo = new PositionTypeVo();
                    BeanUtils.copyProperties(positionType, positionTypeVo);
                    return positionTypeVo;
                })
                .collect(Collectors.groupingBy(PositionTypeVo::getIndustryId));
    }

    /**
     * 刷新 PositionType 缓存 根据 行业id
     */
    private void refreshPositionTypeCacheByIndustryId(Long id) {

    }

    /**
     * 刷新全部的职位缓存
     */
    private void refreshPositionTypeCache() {

    }


}
