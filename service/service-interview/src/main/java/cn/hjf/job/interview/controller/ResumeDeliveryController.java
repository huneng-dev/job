package cn.hjf.job.interview.controller;

import cn.hjf.job.chat.client.ChatRelationshipFeignClient;
import cn.hjf.job.common.result.Result;
import cn.hjf.job.interview.service.ResumeDeliveryService;
import cn.hjf.job.model.document.chat.Message;
import cn.hjf.job.model.document.chat.MessageType;
import cn.hjf.job.model.entity.interview.ResumeDelivery;
import cn.hjf.job.model.vo.base.PageVo;
import cn.hjf.job.model.vo.chat.ChatRelationshipVo;
import cn.hjf.job.model.vo.interview.ResumeDeliveryVo;
import cn.hjf.job.model.vo.resume.ResumeVo;
import cn.hjf.job.resume.client.ResumeInfoFeignClient;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * 简历投递控制器
 *
 * @author hjf
 * @since 2025-02-04
 */
@RestController
@RequestMapping("/resumeDelivery")
public class ResumeDeliveryController {

    @Resource
    private ResumeInfoFeignClient resumeInfoFeignClient;

    @Resource
    private ChatRelationshipFeignClient chatRelationshipFeignClient;

    @Resource
    private ResumeDeliveryService resumeDeliveryService;


    /**
     * 应聘端简历投递
     * 只能投递自己的默认简历
     *
     * @param chatId 聊天关系id
     * @return 简历投递结果
     */
    @PostMapping("/deliver")
    public Result<ResumeDeliveryVo> candidateResumeDelivery(@RequestParam Long chatId) {
        // 获取自己的默认简历
        Result<ResumeVo> baseResume = resumeInfoFeignClient.getBaseResume();
        if (baseResume.getCode() != 200 || baseResume.getData() == null) {
            return Result.fail();
        }

        Long resumeId = baseResume.getData().getId();

        // 判断聊天关系是否属于自己
        Result<ChatRelationshipVo> chatRelationshipByChatId = chatRelationshipFeignClient.getChatRelationshipByChatId(chatId);
        if (chatRelationshipByChatId.getCode() != 200 || chatRelationshipByChatId.getData() == null) {
            return Result.fail();
        }

        // 将投递记录写入数据库
        ResumeDeliveryVo resumeDeliveryVo = resumeDeliveryService.candidateResumeDelivery(resumeId, chatRelationshipByChatId.getData());

        // 判断是否投递成功
        if (resumeDeliveryVo == null) {
            return Result.fail();
        }

        // 通知招聘端
        Message message = new Message();
        message.setChatId(chatRelationshipByChatId.getData().getId());
        message.setContent("SEND_RESUME");
        message.setMessageType(MessageType.SEND_RESUME);
        Result<Message> messageResult = chatRelationshipFeignClient.sendResumeDeliveryMessage(message);
        if (messageResult.getCode() != 200) {
            return Result.fail();
        }
        // 返回投递记录
        return Result.ok(resumeDeliveryVo);
    }

    /**
     * 招聘端获取简历投递投递记录
     *
     * @param candidateId 投递者id
     * @param positionId  职位id
     * @return 投递记录
     */
    @GetMapping("/recruiter/resumeDelivery")
    public Result<ResumeDeliveryVo> getRecruiterIdResumeDeliveryVoByCandidateIdAndPositionId(@RequestParam Long candidateId, @RequestParam Long positionId, Principal principal) {
        ResumeDeliveryVo resumeDeliveryVo = resumeDeliveryService.getRecruiterIdResumeDeliveryVoByCandidateIdAndPositionId(Long.parseLong(principal.getName()), candidateId, positionId);
        return Result.ok(resumeDeliveryVo);
    }

    /**
     * 投递者获取简历投递投递记录
     *
     * @param recruiterId 招聘者id
     * @param positionId  职位id
     * @return 投递记录
     */
    @GetMapping("/candidate/resumeDelivery")
    public Result<ResumeDeliveryVo> getCandidateIdResumeDeliveryVoByRecruiterIdAndPositionId(@RequestParam Long recruiterId, @RequestParam Long positionId, Principal principal) {
        ResumeDeliveryVo resumeDeliveryVo = resumeDeliveryService.getCandidateIdResumeDeliveryVoByRecruiterIdAndPositionId(Long.parseLong(principal.getName()), recruiterId, positionId);
        return Result.ok(resumeDeliveryVo);
    }

    /**
     * 投递者获取简历投递投递记录
     *
     * @param resumeId 简历id
     * @return 简历投递记录
     */
    @GetMapping("/resume/{resumeId}/{positionId}")
    public Result<ResumeDeliveryVo> getResumeDeliveryVoByResumeId(@PathVariable(name = "resumeId") Long resumeId, @PathVariable(name = "positionId") Long positionId, Principal principal) {
        ResumeDeliveryVo resumeDeliveryVo = resumeDeliveryService.getResumeDeliveryVoByResumeId(Long.parseLong(principal.getName()), positionId, resumeId);
        return Result.ok(resumeDeliveryVo);
    }

    /**
     * 投递者获取简历投递投递记录
     *
     * @param page  页码
     * @param limit 每页数量
     * @return 简历投递记录
     */
    @GetMapping("/recruiter/{page}/{limit}")
    public Result<PageVo<ResumeDeliveryVo>> getResumeDeliveryVoPageByUserId(@PathVariable(name = "page") Integer page, @PathVariable(name = "limit") Integer limit, Principal principal) {
        Page<ResumeDelivery> resumeDeliveryPage = new Page<>(page, limit);

        PageVo<ResumeDeliveryVo> resumeDeliveryVoPageVo = resumeDeliveryService.getResumeDeliveryVoPageByUserId(resumeDeliveryPage, Long.parseLong(principal.getName()));

        return resumeDeliveryVoPageVo != null ? Result.ok(resumeDeliveryVoPageVo) : Result.fail();
    }
}
