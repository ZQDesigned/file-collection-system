package com.lnyynet.filecollect.task.config

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
                .title("任务服务 API")
                .version("1.0")
                .description("""
                    任务服务提供以下功能：
                    * 任务管理：创建、查询、更新、删除任务
                    * 提交管理：提交作业、查询提交记录
                    * 文件管理：上传文件、下载文件、批量下载
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