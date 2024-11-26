package com.lnyynet.filecollect.notification.config

import com.lnyynet.filecollect.notification.security.JwtChannelInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity
import org.springframework.security.messaging.context.SecurityContextChannelInterceptor
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
@EnableWebSocketSecurity
class WebSocketConfig(
    private val jwtDecoder: JwtDecoder
) : WebSocketMessageBrokerConfigurer {
    
    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/topic", "/queue")
        registry.setApplicationDestinationPrefixes("/app")
        registry.setUserDestinationPrefix("/user")
    }
    
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws")
            .setAllowedOriginPatterns("*")
            .withSockJS()
    }
    
    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(
            SecurityContextChannelInterceptor(),
            JwtChannelInterceptor(jwtDecoder)
        )
    }
    
    override fun configureClientOutboundChannel(registration: ChannelRegistration) {
        registration.interceptors(
            SecurityContextChannelInterceptor(),
            JwtChannelInterceptor(jwtDecoder)
        )
    }
} 