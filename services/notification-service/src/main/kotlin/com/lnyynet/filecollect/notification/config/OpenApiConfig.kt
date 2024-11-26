package com.lnyynet.filecollect.notification.config

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
                .title("通知服务 API")
                .version("1.0")
                .description("""
                    通知服务提供以下功能：
                    * 邮件通知：发送邮件、管理邮件模板
                    * 站内消息：发送消息、管理消息状态
                    * 实时通知：WebSocket 实时推送
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