package com.lnyynet.filecollect.task.config

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
                "service", "task-service"
            )
        }
    }
    
    @Bean
    fun taskCreatedCounter(registry: MeterRegistry): Counter {
        return Counter.builder("task.created")
            .description("任务创建数量")
            .register(registry)
    }
    
    @Bean
    fun submissionSuccessCounter(registry: MeterRegistry): Counter {
        return Counter.builder("submission.success")
            .description("提交成功数量")
            .register(registry)
    }
    
    @Bean
    fun submissionFailureCounter(registry: MeterRegistry): Counter {
        return Counter.builder("submission.failure")
            .description("提交失败数量")
            .register(registry)
    }
    
    @Bean
    fun fileUploadTimer(registry: MeterRegistry): Timer {
        return Timer.builder("file.upload.duration")
            .description("文件上传耗时")
            .register(registry)
    }
    
    @Bean
    fun fileDownloadTimer(registry: MeterRegistry): Timer {
        return Timer.builder("file.download.duration")
            .description("文件下载耗时")
            .register(registry)
    }
    
    @Bean
    fun apiResponseTimer(registry: MeterRegistry): Timer {
        return Timer.builder("api.response.duration")
            .description("API响应耗时")
            .register(registry)
    }
} 