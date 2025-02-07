package cn.hjf.job.push.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;


/**
 * WebSocket (Stomp) 配置类
 *
 * @author hjf
 * @version 1.0
 * @description
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompProperties stompProperties;

    private final StompAuthInterceptor stompAuthInterceptor;

    @Autowired
    public WebSocketConfig(StompAuthInterceptor stompAuthInterceptor, StompProperties stompProperties) {
        this.stompAuthInterceptor = stompAuthInterceptor;
        this.stompProperties = stompProperties;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 配置 STOMP 消息代理中继（使用 RabbitMQ）topic：群发 , queue：单独发送
        config.enableStompBrokerRelay("/topic", "/queue")
                .setRelayHost(stompProperties.getHost())
                .setRelayPort(stompProperties.getPort())
                .setClientLogin(stompProperties.getLogin())
                .setClientPasscode(stompProperties.getPassword())
                .setVirtualHost(stompProperties.getVHost())
                .setSystemLogin(stompProperties.getLogin())
                .setSystemPasscode(stompProperties.getPassword())
                // 系统心跳间隔调整为更合理的值
                .setSystemHeartbeatReceiveInterval(20000)    // 20秒
                .setSystemHeartbeatSendInterval(20000);// 20秒

        // 设置应用前缀，应用内请求路径
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")  // 配置客户端连接 WebSocket 端点
                .setAllowedOriginPatterns("*")
                .withSockJS()
                .setHeartbeatTime(20000)
                .setDisconnectDelay(30000);  // 配置 SockJS（提供 WebSocket 兼容性）
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(512 * 1024)    // 设置更合理的消息大小限制：512KB
                .setSendTimeLimit(20 * 1000)        // 20秒发送超时
                .setSendBufferSizeLimit(1024 * 1024)// 1MB缓冲区限制
                .setTimeToFirstMessage(30000);      // 30秒首次消息超时
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompAuthInterceptor)
                .taskExecutor()  // 使用默认执行器配置
                .corePoolSize(4)
                .maxPoolSize(8)
                .queueCapacity(50);
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.taskExecutor()  // 使用默认执行器配置
                .corePoolSize(4)
                .maxPoolSize(8)
                .queueCapacity(50);
    }

    @Bean
    public MappingJackson2MessageConverter messageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setSerializedPayloadClass(String.class);
        return converter;
    }

}
