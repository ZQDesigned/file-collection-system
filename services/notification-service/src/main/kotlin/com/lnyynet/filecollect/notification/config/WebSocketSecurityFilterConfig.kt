package com.lnyynet.filecollect.notification.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer

@Configuration
class WebSocketSecurityFilterConfig : AbstractSecurityWebSocketMessageBrokerConfigurer() {
    
    override fun configureInbound(messages: MessageSecurityMetadataSourceRegistry) {
        messages
            .simpDestMatchers("/app/**").authenticated()
            .simpSubscribeDestMatchers("/user/**", "/topic/**").authenticated()
            .anyMessage().authenticated()
    }
    
    override fun sameOriginDisabled(): Boolean {
        return true
    }
} 