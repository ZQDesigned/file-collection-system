package com.lnyynet.filecollect.auth.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.domain.AuditorAware
import org.springframework.context.annotation.Bean
import org.springframework.security.core.context.SecurityContextHolder
import java.util.Optional

@Configuration
@EnableJpaAuditing
class AuditConfig {
    
    @Bean
    fun auditorProvider(): AuditorAware<String> {
        return AuditorAware {
            Optional.ofNullable(SecurityContextHolder.getContext().authentication)
                .map { it.name }
                .or { Optional.of("system") }
        }
    }
} 