package cn.hjf.job.push.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

@Configuration
@EnableWebSocketSecurity
public class WebSocketSecurityConfig {

    @Bean
    public AuthorizationManager<Message<?>> messageAuthorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder messages) {
        messages.simpTypeMatchers(SimpMessageType.CONNECT, SimpMessageType.HEARTBEAT, SimpMessageType.DISCONNECT, SimpMessageType.UNSUBSCRIBE).permitAll()
                // 允许空目标消息（如连接、断开等系统消息）
                .nullDestMatcher().permitAll()
                // 订阅个人队列的鉴权处理 （招聘端，应聘端）
                .simpSubscribeDestMatchers("/queue/*").access(((authentication, message) -> {
                    StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message.getMessage(), StompHeaderAccessor.class);
                    if (accessor != null && accessor.getDestination() != null) {
                        // 获取目标地址
                        String destination = accessor.getDestination();
                        // 获取当前用户ID（在CONNECT时存储的）
                        String userId = authentication.get().getName();
                        // 订阅地址是否合法
                        boolean isAuthorized = destination.contains("/queue/user-" + userId);
                        // 根据匹配结果返回鉴权结果
                        return new AuthorizationDecision(isAuthorized);
                    }
                    return new AuthorizationDecision(false);
                }))
                // 招聘端用户订阅认证
                .simpSubscribeDestMatchers("/topic/recruiter").hasAnyRole("ADMIN_RECRUITER", "EMPLOYEE_RECRUITER")
                // 应聘端用户订阅认证
                .simpSubscribeDestMatchers("/topic/candidate").hasRole("USER_CANDIDATE")
                // 所有客户端全局广播
                .simpSubscribeDestMatchers("/topic/global").hasAnyRole("ADMIN_RECRUITER", "EMPLOYEE_RECRUITER", "USER_CANDIDATE")
                .anyMessage().authenticated();

        return messages.build();
    }


    /**
     * 关闭 Csrf
     *
     * @return ChannelInterceptor
     */
    @Bean
    public ChannelInterceptor csrfChannelInterceptor() {
        return new ChannelInterceptor() {
        };
    }
}
