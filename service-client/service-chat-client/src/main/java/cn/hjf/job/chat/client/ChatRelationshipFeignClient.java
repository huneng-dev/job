package cn.hjf.job.chat.client;

import cn.hjf.job.chat.config.ChatFeignRequestInterceptor;
import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.document.chat.Message;
import cn.hjf.job.model.vo.chat.ChatRelationshipVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;


@FeignClient(value = "service-chat", configuration = ChatFeignRequestInterceptor.class)
public interface ChatRelationshipFeignClient {

    /**
     * 获取聊天关系
     *
     * @param chatId 聊天关系ID
     * @return 聊天关系
     */
    @GetMapping("/chatRelationship/{chatId}")
    Result<ChatRelationshipVo> getChatRelationshipByChatId(@PathVariable(name = "chatId") Long chatId);


    @PostMapping("/message/sendResumeDeliveryMessage")
    Result<Message> sendResumeDeliveryMessage(@RequestBody Message message);

}
