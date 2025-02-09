package cn.hjf.job.chat.controller;

import cn.hjf.job.chat.config.KeyProperties;
import cn.hjf.job.chat.service.ChatRelationshipService;
import cn.hjf.job.chat.service.MessageService;
import cn.hjf.job.chat.service.NotificationService;
import cn.hjf.job.common.logging.core.LogUtils;
import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.document.chat.Message;
import cn.hjf.job.model.entity.chat.ChatRelationship;
import cn.hjf.job.model.vo.base.PageVo;
import cn.hjf.job.model.vo.chat.ChatRelationshipVo;
import cn.hjf.job.model.vo.position.CandidateBasePositionInfoVo;
import cn.hjf.job.model.vo.user.UserInfoAllVo;
import cn.hjf.job.position.client.PositionInfoFeignClient;
import cn.hjf.job.user.client.UserInfoFeignClient;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 聊天关系控制器
 *
 * @author hjf
 * @since 2025-01-23
 */
@RestController
@RequestMapping("/chatRelationship")
public class ChatRelationshipController {

    private static final int MAX_LIMIT = 1000;

    @Resource
    private ChatRelationshipService chatRelationshipService;

    @Resource
    private NotificationService notificationService;

    @Resource
    private MessageService messageService;

    @Resource
    private UserInfoFeignClient userInfoFeignClient;

    @Resource
    private PositionInfoFeignClient positionInfoFeignClient;

    @Resource
    private KeyProperties keyProperties;

    /**
     * 通过招聘者ID和职位ID获取聊天关系
     *
     * @param recruiterId 招聘者ID
     * @param positionId  职位ID
     * @param principal   当前用户信息
     * @return 聊天关系信息
     */
    @PreAuthorize("hasRole('ROLE_USER_CANDIDATE')")
    @GetMapping("/recruiter/{recruiterId}/{positionId}")
    public Result<ChatRelationshipVo> getChatRelationshipByRecruiterId(@PathVariable(name = "recruiterId") Long recruiterId, @PathVariable(name = "positionId") Long positionId, Principal principal) {

        return Result.ok(chatRelationshipService.getChatRelationshipByRecruiterIdAndCandidateId(recruiterId, Long.parseLong(principal.getName()), positionId));
    }

    /**
     * 通过求职者ID和职位ID获取聊天关系
     *
     * @param candidateId 求职者ID
     * @param positionId  职位ID
     * @param principal   当前用户信息
     * @return 聊天关系信息
     */
    @PreAuthorize("hasRole('ROLE_EMPLOYEE_RECRUITER')")
    @GetMapping("/candidate/{candidateId}/{positionId}")
    public Result<ChatRelationshipVo> getChatRelationshipByCandidateId(@PathVariable(name = "candidateId") Long candidateId, @PathVariable(name = "positionId") Long positionId, Principal principal) {

        return Result.ok(chatRelationshipService.getChatRelationshipByRecruiterIdAndCandidateId(Long.parseLong(principal.getName()), candidateId, positionId));
    }


    /**
     * 求职者发起聊天请求
     *
     * @param principal   当前用户信息
     * @param recruiterId 招聘者ID
     * @param positionId  职位ID
     * @return 创建的聊天关系信息
     */
    @PreAuthorize("hasRole('ROLE_USER_CANDIDATE')")
    @GetMapping("/candidateCreate/{recruiterId}/{positionId}")
    public Result<ChatRelationshipVo> candidateCreateChat(Principal principal, @PathVariable(name = "recruiterId") Long recruiterId, @PathVariable(name = "positionId") Long positionId) {
        try {
            // 判断 recruiterId 是否存在
            try {
                Result<UserInfoAllVo> userInfoAllVo = userInfoFeignClient.getUserInfoAllVo(recruiterId, keyProperties.getKey());

                if (!Objects.equals(userInfoAllVo.getCode(), 200)) {
                    return Result.fail();
                }

                if (Objects.equals(userInfoAllVo.getData(), null)) {
                    return Result.fail();
                }
            } catch (Exception e) {
                return Result.fail();
            }
            // 判断 positionId 是否存在
            try {
                Result<CandidateBasePositionInfoVo> positionInfoById = positionInfoFeignClient.getPublicBasePositionInfoById(positionId);
                if (!Objects.equals(positionInfoById.getCode(), 200)) {
                    return Result.fail();
                }

                if (Objects.equals(positionInfoById.getData(), null)) {
                    return Result.fail();
                }
            } catch (Exception e) {
                return Result.fail();
            }

            // 创建聊天关系，传递当前用户ID和招聘者ID
            ChatRelationshipVo relationshipVo = chatRelationshipService.createChat(Long.parseLong(principal.getName()), recruiterId, positionId);

            // 通知用户创建聊天关系
            notificationService.sendCreateNotification(String.valueOf(recruiterId), relationshipVo);
            return Result.ok(relationshipVo);
        } catch (Exception e) {
            return Result.fail();
        }
    }


    /**
     * 用于招聘者发起与候选人的聊天
     *
     * @param principal   当前用户信息，用于获取招聘者的ID
     * @param candidateId 候选人的ID
     * @param positionId  职位的ID
     * @return 返回聊天关系的创建结果
     */
    @PreAuthorize("hasRole('ROLE_EMPLOYEE_RECRUITER')")
    @GetMapping("/recruiterCreate/{candidateId}/{positionId}")
    public Result<ChatRelationshipVo> recruiterCreateChat(Principal principal, @PathVariable(name = "candidateId") Long candidateId, @PathVariable(name = "positionId") Long positionId) {
        try {
            // 判断 recruiterId 是否存在
            try {
                Result<UserInfoAllVo> userInfoAllVo = userInfoFeignClient.getUserInfoAllVo(candidateId, keyProperties.getKey());

                if (!Objects.equals(userInfoAllVo.getCode(), 200)) {
                    return Result.fail();
                }

                if (Objects.equals(userInfoAllVo.getData(), null)) {
                    return Result.fail();
                }
            } catch (Exception e) {
                return Result.fail();
            }

            // 判断 positionId 是否存在
            try {
                Result<CandidateBasePositionInfoVo> positionInfoById = positionInfoFeignClient.getPublicBasePositionInfoById(positionId);
                if (!Objects.equals(positionInfoById.getCode(), 200)) {
                    return Result.fail();
                }

                if (Objects.equals(positionInfoById.getData(), null)) {
                    return Result.fail();
                }
            } catch (Exception e) {
                return Result.fail();
            }

            // 创建聊天关系，传递当前用户ID和招聘者ID
            ChatRelationshipVo relationshipVo = chatRelationshipService.createChat(candidateId, Long.parseLong(principal.getName()), positionId);

            // 通知用户创建聊天关系
            notificationService.sendCreateNotification(String.valueOf(candidateId), relationshipVo);

            return Result.ok(relationshipVo);
        } catch (Exception e) {
            return Result.fail();
        }
    }

    /**
     * 发送消息
     *
     * @param principal 当前用户信息
     * @param message   消息对象
     * @return 发送结果
     */
    @PostMapping("/sendMessage")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE_RECRUITER','ROLE_USER_CANDIDATE')")
    public Result<String> sendMessage(Principal principal, @RequestBody @Valid Message message) {
        try {
            // 获取聊天关系
            ChatRelationshipVo chatRelationship = chatRelationshipService.getChatRelationshipById(message.getChatId());

            if (chatRelationship == null) {
                return Result.fail("聊天关系不存在");
            }

            // 判断关系是否可用
            if (chatRelationship.getBlocked() != 0) {
                return Result.fail("聊天关系已被阻止");
            }

            // 解析用户ID
            String userIdStr = principal.getName();
            if (!userIdStr.matches("\\d+")) {
                return Result.fail("无效的用户ID格式");
            }
            Long userId = Long.parseLong(userIdStr);

            // 判断消息是否属于当前用户
            if (!(chatRelationship.getRecruiterId().equals(userId) || chatRelationship.getCandidateId().equals(userId))) {
                return Result.fail("消息不属于当前用户");
            }

            message.setSenderId(userId);

            if (chatRelationship.getRecruiterId().equals(message.getSenderId())) {
                message.setReceiverId(chatRelationship.getCandidateId());
            } else {
                message.setReceiverId(chatRelationship.getRecruiterId());
            }

            message.setStatus("SEND");

            message.setTimestamp(Instant.now());

            // 保存消息
            Message saveMessage = messageService.saveMessage(message);

            // 通知指定用户
            notificationService.sendMessageNotification(saveMessage);

            return Result.ok();
        } catch (NumberFormatException e) {
            LogUtils.error("用户ID格式转换错误", e);
            return Result.fail("用户ID格式转换错误");
        } catch (Exception e) {
            LogUtils.error("发送消息时发生未知错误", e);
            return Result.fail("发送消息时发生未知错误");
        }
    }


    /**
     * 获取招聘者聊天关系列表
     *
     * @param principal  当前用户信息
     * @param limit      每页数量
     * @param updateTime 更新时间
     * @return 招聘者聊天关系列表
     */
    @PreAuthorize("hasRole('ROLE_EMPLOYEE_RECRUITER')")
    @GetMapping("/recruiter/page/{limit}")
    public Result<List<ChatRelationshipVo>> getRecruiterChatRelationshipList(Principal principal,
                                                                             @PathVariable(name = "limit") Integer limit,
                                                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss") Date updateTime) {

        // 参数验证
        if (limit == null || limit <= 0 || limit > MAX_LIMIT) {
            return Result.fail();
        }

        // 处理时间参数
        if (updateTime == null) {
            updateTime = new Date(); // 默认当前时间
        }


        // 业务逻辑
        try {
            List<ChatRelationshipVo> result = chatRelationshipService.getRecruiterChatRelationshipList(
                    Long.parseLong(principal.getName()),
                    limit,
                    updateTime
            );
            return Result.ok(result);
        } catch (NumberFormatException e) {
            return Result.fail();
        }
    }

    /**
     * 获取应聘者聊天关系列表
     *
     * @param principal  当前用户信息
     * @param limit      每页数量
     * @param updateTime 更新时间
     * @return 候选人聊天关系列表
     */
    @PreAuthorize("hasRole('ROLE_USER_CANDIDATE')")
    @GetMapping("/candidate/page/{limit}")
    public Result<List<ChatRelationshipVo>> getCandidateChatRelationshipList(Principal principal,
                                                                             @PathVariable(name = "limit") Integer limit,
                                                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss") Date updateTime) {

        // 参数验证
        if (limit == null || limit <= 0 || limit > MAX_LIMIT) {
            return Result.fail();
        }

        // 处理时间参数
        if (updateTime == null) {
            updateTime = new Date(); // 默认当前时间
        }


        // 业务逻辑
        try {
            List<ChatRelationshipVo> result = chatRelationshipService.getCandidateChatRelationshipList(
                    Long.parseLong(principal.getName()),
                    limit,
                    updateTime
            );
            return Result.ok(result);
        } catch (NumberFormatException e) {
            return Result.fail();
        }
    }

    /**
     * 获取招聘者聊天关系分页
     *
     * @param principal 当前用户信息
     * @param page      页码
     * @param limit     每页数量
     * @return 招聘者聊天关系分页
     */
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE_RECRUITER')")
    @GetMapping("/page/recruiter/{page}/{limit}")
    public Result<PageVo<ChatRelationshipVo>> getRelationshipPageFormRecruiter(Principal principal,
                                                                               @PathVariable(name = "page") Integer page,
                                                                               @PathVariable(name = "limit") Integer limit) {

        Page<ChatRelationship> chatRelationshipPage = new Page<>(page, limit);

        PageVo<ChatRelationshipVo> relationshipPageFormRecruiter = chatRelationshipService.getRelationshipPageFormRecruiter(Long.parseLong(principal.getName()), chatRelationshipPage);

        return Result.ok(relationshipPageFormRecruiter);
    }

    /**
     * 获取聊天关系
     *
     * @param chatId    聊天关系ID
     * @param principal 当前用户信息
     * @return 聊天关系
     */
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE_RECRUITER','ROLE_USER_CANDIDATE')")
    @GetMapping("/{chatId}")
    public Result<ChatRelationshipVo> getChatRelationshipByChatId(@PathVariable(name = "chatId") Long chatId, Principal principal) {
        try {
            // 获取聊天关系
            ChatRelationshipVo chatRelationship = chatRelationshipService.getChatRelationshipById(chatId);

            if (chatRelationship == null) {
                return Result.fail();
            }

            // 判断关系是否可用
            if (chatRelationship.getBlocked() != 0) {
                return Result.fail();
            }

            // 解析用户ID
            String userIdStr = principal.getName();
            if (!userIdStr.matches("\\d+")) {
                return Result.fail();
            }

            Long userId = Long.parseLong(userIdStr);

            // 判断消息是否属于当前用户
            if (!(chatRelationship.getRecruiterId().equals(userId) || chatRelationship.getCandidateId().equals(userId))) {
                return Result.fail();
            }

            return Result.ok(chatRelationship);

        } catch (Exception e) {
            return Result.fail();
        }
    }
}