package com.lnyynet.filecollect.gateway.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.security.SecurityRequirement
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    
    @Bean
    fun customOpenAPI(): OpenAPI {
        val securitySchemeName = "bearer-jwt"
        return OpenAPI()
            .info(Info()
                .title("网关服务 API")
                .version("1.0")
                .description("""
                    网关服务提供以下功能：
                    * 路由转发：请求路由到各个微服务
                    * 认证鉴权：JWT令牌验证
                    * 限流控制：基于Redis的请求限流
                    * 日志记录：请求响应日志
                """.trimIndent())
            )
            .components(Components()
                .addSecuritySchemes(securitySchemeName, SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                )
            )
            .addSecurityItem(SecurityRequirement().addList(securitySchemeName))
    }
} 