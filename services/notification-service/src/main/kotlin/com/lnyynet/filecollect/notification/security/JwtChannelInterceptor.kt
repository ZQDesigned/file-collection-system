package com.lnyynet.filecollect.notification.security

import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component

@Component
class JwtChannelInterceptor(
    private val jwtDecoder: JwtDecoder
) : ChannelInterceptor {
    
    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*> {
        val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)
        
        if (StompCommand.CONNECT == accessor?.command) {
            val token = accessor.getFirstNativeHeader("Authorization")?.removePrefix("Bearer ")
            if (token != null) {
                val jwt = jwtDecoder.decode(token)
                val authorities = (jwt.claims["authorities"] as? List<*>)?.map { 
                    SimpleGrantedAuthority(it.toString()) 
                } ?: emptyList()
                
                val authentication = JwtAuthenticationToken(jwt, authorities)
                accessor.user = authentication
            }
        }
        
        return message
    }
} 