package com.lnyynet.filecollect.file.config

import io.minio.MinioClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MinioConfig {
    @Value("\${minio.endpoint}")
    private lateinit var endpoint: String
    
    @Value("\${minio.access-key}")
    private lateinit var accessKey: String
    
    @Value("\${minio.secret-key}")
    private lateinit var secretKey: String
    
    @Bean
    fun minioClient(): MinioClient {
        return MinioClient.builder()
            .endpoint(endpoint)
            .credentials(accessKey, secretKey)
            .build()
    }
} 