package com.lnyynet.filecollect.gateway.filter

import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.concurrent.atomic.AtomicLong

@Component
class LoggingFilter : GlobalFilter, Ordered {
    private val requestLogger = LoggerFactory.getLogger("request")
    private val routeLogger = LoggerFactory.getLogger("route")
    
    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val startTime = Instant.now()
        val request = exchange.request
        val path = request.path.value()
        val method = request.method
        val query = request.queryParams
        
        // 记录请求开始
        requestLogger.info(
            "Request started - path: {}, method: {}, query: {}, client: {}",
            path, method, query, request.remoteAddress
        )
        
        return chain.filter(exchange).then(Mono.fromRunnable {
            val duration = Instant.now().toEpochMilli() - startTime.toEpochMilli()
            val response = exchange.response
            val status = response.statusCode?.value() ?: 500
            
            // 记录请求完成
            requestLogger.info(
                "Request completed - path: {}, method: {}, status: {}, duration: {}ms",
                path, method, status, duration
            )
            
            // 记录路由信息
            val route = exchange.getAttribute<String>("route_id")
            routeLogger.info(
                "Route processed - route: {}, path: {}, duration: {}ms, status: {}",
                route, path, duration, status
            )
        })
    }
    
    override fun getOrder(): Int = Ordered.LOWEST_PRECEDENCE
} 