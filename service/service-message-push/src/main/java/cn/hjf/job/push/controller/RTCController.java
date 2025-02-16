package cn.hjf.job.push.controller;

import cn.hjf.job.model.document.chat.Message;
import cn.hjf.job.model.document.chat.RTCIceCandidateInit;
import cn.hjf.job.model.document.chat.RTCSessionDescriptionInit;
import cn.hjf.job.model.entity.chat.MessageWrapper;
import cn.hjf.job.model.entity.chat.MessageWrapperType;
import cn.hjf.job.model.entity.chat.RTCMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Objects;

@Controller
public class RTCController {


    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public RTCController(SimpMessagingTemplate template) {
        this.simpMessagingTemplate = template;
    }

    @MessageMapping("/sdp")
    public void sendRtcMessage(@Payload RTCMessage<RTCSessionDescriptionInit> rtcMessage, Principal principal) {
        // 用于维护当前的会话
        Message message = rtcMessage.getMessage();

        // 当前的 RTCMessage 类型
        String type = rtcMessage.getType();

        // 当前的 RTCMessage 的 payload
        RTCSessionDescriptionInit payload = rtcMessage.getPayload();

        // 解析用户ID
        String userIdStr = principal.getName();
        if (!userIdStr.matches("\\d+")) {
            return;
        }
        Long userId = Long.parseLong(userIdStr);

        // 1. 判断类型
        if (!Objects.equals(type, "offer") && !Objects.equals(type, "answer")) {
            return;
        }

        // 2. 判断当前消息是否与 当前用户有关
        if (!(message.getSenderId().equals(userId) || message.getReceiverId().equals(userId))) {
            return;
        }

        // 3. 确定目标id
        Long targetId;
        if (Objects.equals(userId, message.getSenderId())) {
            targetId = message.getReceiverId();
        } else {
            targetId = message.getSenderId();
        }

        // 4. 发送 SDP 到目标用户
        // 4.1 封装消息
        MessageWrapper<RTCMessage<RTCSessionDescriptionInit>> rtcMessageMessageWrapper = new MessageWrapper<>();
        rtcMessageMessageWrapper.setMessageWrapperType(MessageWrapperType.RTC);
        rtcMessageMessageWrapper.setData(new RTCMessage<>(type, payload, message));

        // 4.2 发送消息
        simpMessagingTemplate.convertAndSend("/queue/user-" + targetId, rtcMessageMessageWrapper);
    }

    @MessageMapping("/ice")
    public void sendIceCandidate(RTCMessage<RTCIceCandidateInit> rtcMessage, Principal principal) {
        System.out.println("rtcMessage = " + rtcMessage + ", principal = " + principal);
        // 用于维护当前的会话
        Message message = rtcMessage.getMessage();

        // 当前的 RTCMessage 类型
        String type = rtcMessage.getType();

        // 当前的 RTCMessage 的 payload
        RTCIceCandidateInit payload = rtcMessage.getPayload();

        // 解析用户ID
        String userIdStr = principal.getName();
        if (!userIdStr.matches("\\d+")) {
            return;
        }
        Long userId = Long.parseLong(userIdStr);

        // 1. 判断类型
        if (!Objects.equals(type, "candidate")) {
            return;
        }

        // 2. 判断当前消息是否与 当前用户有关
        if (!(message.getSenderId().equals(userId) || message.getReceiverId().equals(userId))) {
            return;
        }

        // 3. 确定目标id
        Long targetId;
        if (Objects.equals(userId, message.getSenderId())) {
            targetId = message.getReceiverId();
        } else {
            targetId = message.getSenderId();
        }

        // 4. 发送 SDP 到目标用户
        // 4.1 封装消息
        MessageWrapper<RTCMessage<RTCIceCandidateInit>> rtcMessageMessageWrapper = new MessageWrapper<>();
        rtcMessageMessageWrapper.setMessageWrapperType(MessageWrapperType.RTC);
        rtcMessageMessageWrapper.setData(new RTCMessage<>(type, payload, message));

        // 4.2 发送消息
        simpMessagingTemplate.convertAndSend("/queue/user-" + targetId, rtcMessageMessageWrapper);
    }
}
