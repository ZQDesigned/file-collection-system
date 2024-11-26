package com.lnyynet.filecollect.gateway.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "security")
data class SecurityProperties(
    val ignorePaths: List<String> = listOf(
        "/api/auth/login",
        "/api/auth/register",
        "/v3/api-docs",
        "/swagger-ui",
        "/actuator"
    )
) 