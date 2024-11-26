package com.lnyynet.filecollect.gateway.config

import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.*

@Configuration
class ErrorHandlerConfig {
    
    @Bean
    fun errorAttributes() = DefaultErrorAttributes()
    
    @Bean
    fun errorRouter(errorHandler: ErrorHandler) = coRouter {
        GET("/error", errorHandler::handle)
        POST("/error", errorHandler::handle)
    }
}

@Configuration
class ErrorHandler(private val errorAttributes: DefaultErrorAttributes) {
    
    suspend fun handle(request: ServerRequest): ServerResponse {
        val error = errorAttributes.getErrorAttributes(request, ErrorAttributeOptions.defaults())
        return ServerResponse.status(error["status"] as Int)
            .json()
            .bodyValueAndAwait(error)
    }
} 