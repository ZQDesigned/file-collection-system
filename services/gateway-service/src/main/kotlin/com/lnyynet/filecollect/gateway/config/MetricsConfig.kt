package com.lnyynet.filecollect.gateway.config

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MetricsConfig {
    
    @Bean
    fun metricsCommonTags(): MeterRegistryCustomizer<MeterRegistry> {
        return MeterRegistryCustomizer { registry ->
            registry.config().commonTags(
                "service", "gateway-service"
            )
        }
    }
    
    @Bean
    fun requestCounter(registry: MeterRegistry): Counter {
        return Counter.builder("gateway.requests.total")
            .description("网关请求总数")
            .register(registry)
    }
    
    @Bean
    fun authFailureCounter(registry: MeterRegistry): Counter {
        return Counter.builder("gateway.auth.failures")
            .description("认证失败次数")
            .register(registry)
    }
    
    @Bean
    fun rateLimitCounter(registry: MeterRegistry): Counter {
        return Counter.builder("gateway.ratelimit.exceeded")
            .description("限流触发次数")
            .register(registry)
    }
    
    @Bean
    fun routeTimer(registry: MeterRegistry): Timer {
        return Timer.builder("gateway.route.duration")
            .description("路由转发耗时")
            .register(registry)
    }
    
    @Bean
    fun serviceTimer(registry: MeterRegistry): Timer {
        return Timer.builder("gateway.service.duration")
            .description("服务调用耗时")
            .tags("service", "unknown")
            .register(registry)
    }
} 