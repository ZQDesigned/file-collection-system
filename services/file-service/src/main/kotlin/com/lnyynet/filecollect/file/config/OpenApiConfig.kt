package com.lnyynet.filecollect.file.config

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
                .title("文件服务 API")
                .version("1.0")
                .description("""
                    文件服务提供以下功能：
                    * 文件上传：支持单文件和多文件上传
                    * 文件下载：支持文件下载和预览
                    * 文件管理：文件信息查询、删除
                    * gRPC服务：提供文件操作接口
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