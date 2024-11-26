package com.lnyynet.filecollect.notification.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import org.springframework.security.authorization.AuthorizationManager
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager

@Configuration
@EnableWebSocketSecurity
class WebSocketSecurityConfig {
    
    @Bean
    fun messageAuthorizationManager(): AuthorizationManager<Message<*>> {
        val messages = MessageMatcherDelegatingAuthorizationManager.builder()
            .simpDestMatchers("/app/**").authenticated()
            .simpSubscribeDestMatchers("/user/**", "/topic/**").authenticated()
            .anyMessage().authenticated()
            .build()
        return messages
    }
} 