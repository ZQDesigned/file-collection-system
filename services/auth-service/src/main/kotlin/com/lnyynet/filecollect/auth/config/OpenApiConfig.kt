package com.lnyynet.filecollect.auth.config

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
                .title("认证服务 API")
                .version("1.0")
                .description("""
                    认证服务提供以下功能：
                    * 用户管理：注册、查询、更新用户信息
                    * 认证授权：登录、刷新令牌、修改密码
                    * gRPC服务：提供认证验证接口
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