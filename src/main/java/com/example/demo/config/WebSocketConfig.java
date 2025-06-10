package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  @Override //註冊STOMP端點讓前端訂閱
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/chat-websocket")
        .setAllowedOriginPatterns("http://localhost:5173")
        .withSockJS();
  }

  @Override //定義訊息發送與接收方式
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker("/topic");
    registry.setApplicationDestinationPrefixes("/app"); //發送訊息前綴
  }
}
