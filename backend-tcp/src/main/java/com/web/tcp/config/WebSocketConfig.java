package com.web.tcp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@Controller
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    // 클라이언트가 메시지를 구독할 endpoint를 정의
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/server");
        config.enableSimpleBroker("/client");
    }//

    @Override
    // connection을 맺을때 CORS 허용
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/tcpapi/socketalarm").setAllowedOrigins("http://k5f006.p.ssafy.io").withSockJS();
        registry.addEndpoint("/todo").setAllowedOrigins("https://k5f006.p.ssafy.io").withSockJS();
    }

}