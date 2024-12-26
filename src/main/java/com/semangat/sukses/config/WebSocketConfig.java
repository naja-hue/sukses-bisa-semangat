package com.semangat.sukses.config;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(@NotNull MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Enable a simple message broker
        config.setApplicationDestinationPrefixes("/app"); // Set the application destination prefix
    }


    @Override
    public void registerStompEndpoints(@NotNull StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS(); // Register the "/ws" endpoint with SockJS
    }
}