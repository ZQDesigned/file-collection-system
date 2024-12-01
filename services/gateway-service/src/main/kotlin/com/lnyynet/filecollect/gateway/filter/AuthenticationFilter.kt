package com.lnyynet.filecollect.gateway.filter

import com.lnyynet.filecollect.gateway.config.SecurityProperties
import com.lnyynet.filecollect.gateway.service.JwtService
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono


class AuthenticationFilter(
    private val jwtService: JwtService,
    private val securityProperties: SecurityProperties
) : GlobalFilter, Ordered {

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val request = exchange.request
        val path = request.path.value()
        
        // 跳过不需要认证的路径
        if (securityProperties.ignorePaths.any { path.startsWith(it) }) {
            return chain.filter(exchange)
        }
        
        val token = request.headers.getFirst("Authorization")
            ?.takeIf { it.startsWith("Bearer ") }
            ?.substring(7)
            
        if (token == null) {
            exchange.response.statusCode = HttpStatus.UNAUTHORIZED
            return exchange.response.setComplete()
        }
        
        return try {
            val claims = jwtService.validateToken(token)
            val modifiedRequest = exchange.request.mutate()
                .header("X-User-Id", claims.subject)
                .header("X-User-Roles", claims.get("roles", List::class.java).joinToString(","))
                .build()
                
            chain.filter(exchange.mutate().request(modifiedRequest).build())
        } catch (e: Exception) {
            exchange.response.statusCode = HttpStatus.UNAUTHORIZED
            exchange.response.setComplete()
        }
    }

    override fun getOrder(): Int = -100
} 