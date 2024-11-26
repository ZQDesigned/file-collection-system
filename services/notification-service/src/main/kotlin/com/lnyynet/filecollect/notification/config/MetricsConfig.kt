package com.lnyynet.filecollect.notification.config

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
                "service", "notification-service"
            )
        }
    }
    
    @Bean
    fun messageCreatedCounter(registry: MeterRegistry): Counter {
        return Counter.builder("message.created")
            .description("消息创建数量")
            .register(registry)
    }
    
    @Bean
    fun messageReadCounter(registry: MeterRegistry): Counter {
        return Counter.builder("message.read")
            .description("消息已读数量")
            .register(registry)
    }
    
    @Bean
    fun messageArchivedCounter(registry: MeterRegistry): Counter {
        return Counter.builder("message.archived")
            .description("消息归档数量")
            .register(registry)
    }
    
    @Bean
    fun websocketConnectionCounter(registry: MeterRegistry): Counter {
        return Counter.builder("websocket.connections")
            .description("WebSocket连接数量")
            .register(registry)
    }
    
    @Bean
    fun websocketMessageCounter(registry: MeterRegistry): Counter {
        return Counter.builder("websocket.messages")
            .description("WebSocket消息数量")
            .register(registry)
    }
    
    @Bean
    fun apiResponseTimer(registry: MeterRegistry): Timer {
        return Timer.builder("api.response.duration")
            .description("API响应耗时")
            .register(registry)
    }
} 