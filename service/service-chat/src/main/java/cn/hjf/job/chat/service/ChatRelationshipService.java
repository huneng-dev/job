package cn.hjf.job.chat.service;

import cn.hjf.job.model.entity.chat.ChatRelationship;
import cn.hjf.job.model.vo.base.PageVo;
import cn.hjf.job.model.vo.chat.ChatRelationshipVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 聊天关系表 服务类
 * </p>
 *
 * @author hjf
 * @since 2025-01-23
 */
public interface ChatRelationshipService extends IService<ChatRelationship> {

    /**
     * 创建聊天关系
     * 此方法用于在候选人、招聘人员和职位之间建立聊天关系它创建了一个聊天环境，
     * 允许招聘人员和候选人就特定职位进行沟通
     *
     * @param candidateId 候选人的唯一标识符
     * @param recruiterId 招聘人员的唯一标识符
     * @param positionId  职位的唯一标识符
     * @return 返回一个ChatRelationshipVo对象，其中包含建立的聊天关系信息
     */
    ChatRelationshipVo createChat(Long candidateId, Long recruiterId, Long positionId);

    /**
     * 根据招聘者ID、候选人ID和职位ID获取聊天关系
     *
     * @param recruiterId 招聘者ID，用于标识招聘者
     * @param candidateId 候选人ID，用于标识候选人
     * @param positionId  职位ID，用于标识特定职位
     * @return 返回一个ChatRelationshipVo对象，表示招聘者和候选人之间的聊天关系如果找不到匹配的聊天关系，则返回null
     */
    ChatRelationshipVo getChatRelationshipByRecruiterIdAndCandidateId(Long recruiterId, Long candidateId, Long positionId);

    /**
     * 根据聊天关系ID获取聊天关系
     *
     * @param id 聊天关系ID，用于标识特定的聊天关系
     * @return 返回一个ChatRelationshipVo对象，表示聊天关系信息如果找不到匹配的聊天关系，则返回null
     */
    ChatRelationshipVo getChatRelationshipById(Long id);

    /**
     * 获取招聘者聊天关系列表
     *
     * @param recruiterId 招聘者ID，用于标识招聘者
     * @param limit       限制返回的聊天关系数量，用于分页
     * @param updateTime  更新时间，用于筛选更新的聊天关系
     * @return 返回一个ChatRelationshipVo列表，表示招聘者和候选人之间的聊天关系如果找不到匹配的聊天关系，则返回空列表
     */
    List<ChatRelationshipVo> getRecruiterChatRelationshipList(Long recruiterId, Integer limit, Date updateTime);


    /**
     * 获取候选人聊天关系列表
     *
     * @param candidateId 候选人ID，用于标识候选人
     * @param limit       限制返回的聊天关系数量，用于分页
     * @param updateTime  更新时间，用于筛选更新的聊天关系
     * @return 返回一个ChatRelationshipVo列表，表示候选人和招聘者和职位之间的聊天关系如果找不到匹配的聊天关系，则返回空列表
     */
    List<ChatRelationshipVo> getCandidateChatRelationshipList(Long candidateId, Integer limit, Date updateTime);


    PageVo<ChatRelationshipVo> getRelationshipPageFormRecruiter(Long recruiterId, Page<ChatRelationship> chatRelationshipPage);

    ChatRelationshipVo alterChatRelationShipBlock(Long userId, Long chatId);
}
