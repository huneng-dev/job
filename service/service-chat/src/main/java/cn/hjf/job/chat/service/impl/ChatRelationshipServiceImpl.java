package cn.hjf.job.chat.service.impl;

import cn.hjf.job.chat.mapper.ChatRelationshipMapper;
import cn.hjf.job.chat.service.ChatRelationshipService;
import cn.hjf.job.common.constant.RedisConstant;
import cn.hjf.job.common.logging.core.LogUtils;
import cn.hjf.job.model.entity.chat.ChatRelationship;
import cn.hjf.job.model.vo.base.PageVo;
import cn.hjf.job.model.vo.chat.ChatRelationshipVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 聊天关系表 服务实现类
 * </p>
 *
 * @author hjf
 * @since 2025-01-23
 */
@Service
@Slf4j
public class ChatRelationshipServiceImpl extends ServiceImpl<ChatRelationshipMapper, ChatRelationship> implements ChatRelationshipService {

    @Resource
    private ChatRelationshipMapper chatRelationshipMapper;

    @Resource
    private RedisTemplate<String, ChatRelationship> redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public ChatRelationshipVo createChat(Long candidateId, Long recruiterId, Long positionId) {
        try {
            ChatRelationship chatRelationship = new ChatRelationship();
            chatRelationship.setCandidateId(candidateId);
            chatRelationship.setRecruiterId(recruiterId);
            chatRelationship.setPositionId(positionId);
            chatRelationship.setBlocked(0);
            chatRelationship.setDeletedByCandidate(0);
            chatRelationship.setDeletedByRecruiter(0);
            chatRelationship.setRelationshipType(0);
            int insert = chatRelationshipMapper.insert(chatRelationship);

            if (insert != 1) {
                LogUtils.error("创建聊天关系失败，recruiterId: {}, candidateId: {}, positionId: {}", recruiterId, candidateId, positionId);
                throw new RuntimeException("Failed to insert chat relationship");
            }

            LogUtils.info("创建聊天关系成功，recruiterId: {}, candidateId: {}, positionId: {}", recruiterId, candidateId, positionId);
            ChatRelationshipVo chatRelationshipVo = new ChatRelationshipVo();
            BeanUtils.copyProperties(chatRelationship, chatRelationshipVo);
            chatRelationshipVo.setUpdateTime(new Date());
            return chatRelationshipVo;
        } catch (IllegalArgumentException e) {
            LogUtils.error("参数校验失败：{}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LogUtils.error("创建聊天关系时发生异常：", e);
            throw new RuntimeException("创建聊天关系时发生异常", e);
        }
    }

    @Override
    public ChatRelationshipVo getChatRelationshipByRecruiterIdAndCandidateId(Long recruiterId, Long candidateId, Long positionId) {
        LambdaQueryWrapper<ChatRelationship> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(ChatRelationship::getRecruiterId, recruiterId)
                .eq(ChatRelationship::getCandidateId, candidateId)
                .eq(ChatRelationship::getPositionId, positionId);

        try {
            ChatRelationship chatRelationship = chatRelationshipMapper.selectOne(queryWrapper);
            if (chatRelationship == null) {
                LogUtils.warn("未找到聊天关系，recruiterId: {}, candidateId: {}", recruiterId, candidateId);
                return null;
            }

            ChatRelationshipVo chatRelationshipVo = new ChatRelationshipVo();
            BeanUtils.copyProperties(chatRelationship, chatRelationshipVo);
            return chatRelationshipVo;
        } catch (MyBatisSystemException e) {
            LogUtils.error("MyBatis系统异常，获取聊天关系失败，recruiterId: {}, candidateId: {}", e, recruiterId, candidateId);
            throw new RuntimeException("数据库查询失败", e);
        } catch (Exception e) {
            LogUtils.error("未知异常，获取聊天关系失败，recruiterId: {}, candidateId: {}", e, recruiterId, candidateId);
            throw new RuntimeException("获取聊天关系失败", e);
        }
    }

    @Override
    public ChatRelationshipVo getChatRelationshipById(Long id) {
        // 从缓存中获取聊天关系
        ChatRelationship chatRelationship = redisTemplate.opsForValue().get(RedisConstant.CHAT_RELATION_CACHE + id);
        if (chatRelationship != null) {
            return convertToVo(chatRelationship);
        }

        String redisLockKey = RedisConstant.CHAT_RELATION_LOCK + id;
        RLock lock = redissonClient.getLock(redisLockKey);
        try {
            if (lock.tryLock(RedisConstant.CHAT_RELATION_LOCK_WAIT_TIME, RedisConstant.CHAT_RELATION_LOCK_LEASE_TIME, TimeUnit.SECONDS)) {

                // 再次检查缓存，防止其他线程已经缓存
                chatRelationship = redisTemplate.opsForValue().get(RedisConstant.CHAT_RELATION_CACHE + id);
                if (chatRelationship != null) {
                    return convertToVo(chatRelationship);
                }

                // 从数据库中获取聊天关系
                ChatRelationship chatRelationshipFormDB = chatRelationshipMapper.selectById(id);

                if (chatRelationshipFormDB == null) {
                    LogUtils.warn("未找到聊天关系，id: {}", id);
                    // 缓存空对象，防止缓存穿透
                    redisTemplate.opsForValue().set(RedisConstant.CHAT_RELATION_CACHE + id, null, 60, TimeUnit.SECONDS);
                    return null;
                }

                // 将聊天关系缓存到Redis中
                cacheChatRelationship(chatRelationshipFormDB);

                return convertToVo(chatRelationshipFormDB);
            }
        } catch (Exception e) {
            LogUtils.error("获取聊天关系失败，id: {}, 错误信息: {}", id, e.getMessage(), e);
        } finally {
            lock.unlock();
        }
        return null;
    }

    @Override
    public List<ChatRelationshipVo> getRecruiterChatRelationshipList(Long recruiterId, Integer limit, Date updateTime) {
        // 校验输入参数
        if (recruiterId == null || limit == null || limit <= 0) {
            throw new IllegalArgumentException("Invalid input parameters");
        }

        // 设置参数
        LambdaQueryWrapper<ChatRelationship> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatRelationship::getRecruiterId, recruiterId)
                .eq(ChatRelationship::getDeletedByRecruiter, 0)
                .orderByDesc(ChatRelationship::getUpdateTime);

        if (updateTime != null) {
            queryWrapper.lt(ChatRelationship::getUpdateTime, updateTime);
        }

        queryWrapper.last("LIMIT " + limit);

        return getChatRelationshipList(queryWrapper);
    }

    @Override
    public List<ChatRelationshipVo> getCandidateChatRelationshipList(Long candidateId, Integer limit, Date updateTime) {
        if (candidateId == null || limit == null || limit <= 0) {
            throw new IllegalArgumentException("Invalid input parameters");
        }

        LambdaQueryWrapper<ChatRelationship> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatRelationship::getCandidateId, candidateId)
                .eq(ChatRelationship::getDeletedByCandidate, 0)
                .orderByDesc(ChatRelationship::getUpdateTime);

        if (updateTime != null) {
            queryWrapper.lt(ChatRelationship::getUpdateTime, updateTime);
        }

        queryWrapper.last("LIMIT " + limit);

        return getChatRelationshipList(queryWrapper);
    }

    @Override
    public PageVo<ChatRelationshipVo> getRelationshipPageFormRecruiter(Long recruiterId, Page<ChatRelationship> chatRelationshipPage) {
        LambdaQueryWrapper<ChatRelationship> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(ChatRelationship::getRecruiterId, recruiterId)
                .eq(ChatRelationship::getBlocked, 0)
                .orderByDesc(ChatRelationship::getCreateTime);

        Page<ChatRelationship> relationshipPage = chatRelationshipMapper.selectPage(chatRelationshipPage, queryWrapper);
        if (relationshipPage == null) {
            return null;
        }

        // 创建 PageVo 对象
        PageVo<ChatRelationshipVo> pageVo = new PageVo<>();
        pageVo.setPage(relationshipPage.getCurrent());
        pageVo.setLimit(relationshipPage.getSize());
        pageVo.setTotal(relationshipPage.getTotal());
        pageVo.setPages(relationshipPage.getPages());
        pageVo.setRecords(relationshipPage.getRecords().stream().map(this::convertToVo).toList());

        return pageVo;
    }

    @Override
    public ChatRelationshipVo alterChatRelationShipBlock(Long userId, Long chatId) {
        // 获取聊天关系
        ChatRelationshipVo chatRelationshipVo = getChatRelationshipById(chatId);

        // 判断关系是否存在
        if (chatRelationshipVo == null) {
            return null;
        }

        int blocked;

        // 判断当前用户是否为聊天关系中的任何一方
        if (chatRelationshipVo.getRecruiterId().equals(userId)) {
            // 根据规则设置状态
            if (chatRelationshipVo.getBlocked().equals(0)) {
                blocked = 1;
            } else if (chatRelationshipVo.getBlocked().equals(1)) {
                blocked = 0;
            } else if (chatRelationshipVo.getBlocked().equals(2)) {
                blocked = 3;
            } else if (chatRelationshipVo.getBlocked().equals(3)) {
                blocked = 2;
            } else {
                return null;
            }
            // 更新聊天关系
            Boolean isSuccess = updateBlockStatus(chatId, blocked);

            chatRelationshipVo.setBlocked(blocked);

            return isSuccess ? chatRelationshipVo : null;

        }

        if (chatRelationshipVo.getCandidateId().equals(userId)) {
            // 根据规则设置状态
            if (chatRelationshipVo.getBlocked().equals(0)) {
                blocked = 2;
            } else if (chatRelationshipVo.getBlocked().equals(1)) {
                blocked = 3;
            } else if (chatRelationshipVo.getBlocked().equals(2)) {
                blocked = 0;
            } else if (chatRelationshipVo.getBlocked().equals(3)) {
                blocked = 1;
            } else {
                return null;
            }

            // 更新聊天关系
            Boolean isSuccess = updateBlockStatus(chatId, blocked);

            chatRelationshipVo.setBlocked(blocked);

            return isSuccess ? chatRelationshipVo : null;
        }
        return null;
    }

    private Boolean updateBlockStatus(Long chatId, int blocked) {
        LambdaUpdateWrapper<ChatRelationship> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(ChatRelationship::getBlocked, blocked)
                .eq(ChatRelationship::getId, chatId);
        int update = chatRelationshipMapper.update(lambdaUpdateWrapper);

        return update == 1;
    }


    private List<ChatRelationshipVo> getChatRelationshipList(LambdaQueryWrapper<ChatRelationship> queryWrapper) {
        List<ChatRelationship> chatRelationshipList = chatRelationshipMapper.selectList(queryWrapper);
        return chatRelationshipList.stream().map(this::convertToVo).toList();
    }

    private ChatRelationshipVo convertToVo(ChatRelationship chatRelationship) {
        ChatRelationshipVo chatRelationshipVo = new ChatRelationshipVo();
        BeanUtils.copyProperties(chatRelationship, chatRelationshipVo);
        return chatRelationshipVo;
    }

    private void cacheChatRelationship(ChatRelationship chatRelationship) {
        redisTemplate.opsForValue().set(
                RedisConstant.CHAT_RELATION_CACHE + chatRelationship.getId().toString(),
                chatRelationship,
                RedisConstant.CHAT_RELATION_CACHE_TIME,
                TimeUnit.SECONDS
        );
    }
}
