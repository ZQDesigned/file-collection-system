package com.lnyynet.filecollect.gateway.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .authorizeExchange {
                it.pathMatchers(
                    "/swagger-ui.html", 
                    "/swagger-ui/**", 
                    "/v3/api-docs/**", 
                    "/webjars/**",
                    "/*/swagger-ui/**",
                    "/*/v3/api-docs/**",
                ).permitAll()
                .anyExchange().permitAll()
            }
            .csrf().disable()
            .build()
    }
}