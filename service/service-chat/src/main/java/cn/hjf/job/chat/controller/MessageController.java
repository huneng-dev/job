package cn.hjf.job.chat.controller;

import cn.hjf.job.chat.service.ChatRelationshipService;
import cn.hjf.job.chat.service.MessageService;
import cn.hjf.job.chat.service.NotificationService;
import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.document.chat.Message;
import cn.hjf.job.model.document.chat.MessageType;
import cn.hjf.job.model.vo.chat.ChatRelationshipVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Instant;
import java.util.List;

/**
 * 消息控制器
 *
 * @author hjf
 * @version 1.0
 * @description
 */

@RestController
@RequestMapping("/message")
public class MessageController {

    @Resource
    private ChatRelationshipService chatRelationshipService;

    @Resource
    private MessageService messageService;

    @Resource
    private NotificationService notificationService;


    /**
     * 发送文本消息接口
     * 该接口允许用户发送消息，但仅限于招聘人员和求职者角色
     *
     * @param principal 当前用户信息，用于获取发送者ID
     * @param message   待发送的消息对象，包括消息内容和聊天ID
     * @return 返回消息发送结果，包括成功或失败状态
     */
    @PostMapping("/sendTextMessage")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE_RECRUITER','ROLE_USER_CANDIDATE')")
    public Result<Message> sendTextMessage(Principal principal, @RequestBody @Valid Message message) {
        try {
            if (!message.getMessageType().equals(MessageType.TEXT)) {
                return Result.fail();
            }

            // 获取聊天关系
            ChatRelationshipVo chatRelationship = chatRelationshipService.getChatRelationshipById(message.getChatId());

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

            return Result.ok(saveMessage);
        } catch (Exception e) {
            return Result.fail();
        }
    }

    /**
     * 获取聊天关系中的最新一条消息
     *
     * @param chatId 聊天关系ID
     * @return 返回消息对象，如果聊天关系不存在或没有消息则返回失败结果
     */
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE_RECRUITER','ROLE_USER_CANDIDATE')")
    @GetMapping("/FirstMessage/{chatId}")
    public Result<Message> getLatestMessageByChatId(@PathVariable(name = "chatId") Long chatId, Principal principal) {
        try {
            // 判断聊天关系是否存在
            ChatRelationshipVo relationship = chatRelationshipService.getChatRelationshipById(chatId);
            if (relationship == null) {
                return Result.fail();
            }

            // 判断聊天关系是否属于当前用户
            if (!(relationship.getRecruiterId().equals(Long.parseLong(principal.getName())) || relationship.getCandidateId().equals(Long.parseLong(principal.getName())))) {
                return Result.fail();
            }
            Message message = messageService.getLatestMessageByChatId(chatId);
            return Result.ok(message);
        } catch (Exception e) {
            return Result.fail();
        }
    }


    /**
     * 获取聊天关系中的历史消息
     *
     * @param chatId     聊天关系ID
     * @param pageSize   每页消息数量
     * @param beforeTime 获取消息的起始时间
     * @param principal  当前用户信息，用于获取发送者ID
     * @return 返回消息列表，如果聊天关系不存在或没有消息则返回失败结果
     */
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE_RECRUITER','ROLE_USER_CANDIDATE')")
    @GetMapping("/HistoryMessage/{chatId}/{pageSize}")
    public Result<List<Message>> getHistoryMessageByChatId(
            @PathVariable(name = "chatId") Long chatId,
            @PathVariable(name = "pageSize") Integer pageSize,
            @RequestParam(required = false) Instant beforeTime,
            Principal principal
    ) {
        try {
            // 判断聊天关系是否存在
            ChatRelationshipVo relationship = chatRelationshipService.getChatRelationshipById(chatId);
            if (relationship == null) {
                return Result.fail();
            }

            // 判断聊天关系是否属于当前用户
            if (!(relationship.getRecruiterId().equals(Long.parseLong(principal.getName())) || relationship.getCandidateId().equals(Long.parseLong(principal.getName())))) {
                return Result.fail();
            }

            List<Message> messageList = messageService.getHistoryMessageByChatId(chatId, pageSize, beforeTime);
            return Result.ok(messageList);
        } catch (Exception e) {
            return Result.fail();
        }
    }

    /**
     * 已读消息接口
     * 携带聊天记录中的任意一条消息，即可标记对方信息全部已读
     *
     * @param message   消息对象，包含需要标记为已读的消息的聊天ID
     * @param principal 用户信息，用于获取当前操作用户ID
     * @return 返回标记消息是否成功的结果
     */
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE_RECRUITER','ROLE_USER_CANDIDATE')")
    @PutMapping("/read")
    public Result<Boolean> setReadMessage(@RequestBody @Valid Message message, Principal principal) {

        try {
            Long userId = Long.parseLong(principal.getName());

            // 判断聊天关系是否存在
            ChatRelationshipVo relationship = chatRelationshipService.getChatRelationshipById(message.getChatId());
            if (relationship == null) {
                return Result.fail();
            }

            // 判断聊天关系是否属于当前用户
            if (!(relationship.getRecruiterId().equals(userId) || relationship.getCandidateId().equals(userId))) {
                return Result.fail();
            }

            // Tip: 这里的senderId 永远是对方ID
            Long senderId = relationship.getRecruiterId().equals(userId) ? relationship.getCandidateId() : relationship.getRecruiterId();

            Boolean isSuccess = messageService.setReadMessage(message.getChatId(), senderId);

            notificationService.sendReadNotification(message.getChatId(), senderId);
            return Result.ok(isSuccess);
        } catch (NumberFormatException e) {
            return Result.fail();
        }
    }

    /**
     * 发送图片消息
     *
     * @param principal 用户信息，用于获取当前操作用户ID
     * @param message   消息对象，包含消息内容、发送者ID、接收者ID、消息类型、附件URL、状态、时间戳等属性
     * @return 返回发送的消息对象，如果发送失败则返回失败结果
     */
    @PostMapping("/sendImageMessage")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE_RECRUITER','ROLE_USER_CANDIDATE')")
    public Result<Message> sendImageMessage(Principal principal, @RequestBody @Valid Message message) {
        try {
            if (!message.getMessageType().equals(MessageType.IMAGE)) {
                return Result.fail();
            }

            // 获取聊天关系
            ChatRelationshipVo chatRelationship = chatRelationshipService.getChatRelationshipById(message.getChatId());

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

            return Result.ok(saveMessage);
        } catch (Exception e) {
            return Result.fail();
        }
    }

    /**
     * 发送文件消息
     *
     * @param principal 用户信息，用于获取当前操作用户ID
     * @param message   消息对象，包含消息内容、发送者ID、接收者ID、消息类型、附件URL、状态、时间戳等属性
     * @return 返回发送的消息对象，如果发送失败则返回失败结果
     */
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE_RECRUITER','ROLE_USER_CANDIDATE')")
    @PostMapping("/sendFileMessage")
    public Result<Message> sendFileMessage(Principal principal, @RequestBody @Valid Message message) {
        try {
            if (!message.getMessageType().equals(MessageType.WORD) &&
                    !message.getMessageType().equals(MessageType.EXCEL) &&
                    !message.getMessageType().equals(MessageType.PPT) &&
                    !message.getMessageType().equals(MessageType.PDF) &&
                    !message.getMessageType().equals(MessageType.ZIP) &&
                    !message.getMessageType().equals(MessageType.FILE)) {
                return Result.fail();
            }

            // 获取聊天关系
            ChatRelationshipVo chatRelationship = chatRelationshipService.getChatRelationshipById(message.getChatId());

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

            return Result.ok(saveMessage);
        } catch (Exception e) {
            return Result.fail();
        }
    }
}
