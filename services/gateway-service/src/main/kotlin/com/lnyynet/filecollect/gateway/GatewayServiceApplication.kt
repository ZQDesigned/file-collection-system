package com.lnyynet.filecollect.gateway

import com.lnyynet.filecollect.gateway.config.SecurityProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication(scanBasePackages = ["com.lnyynet.filecollect.gateway", "com.lnyynet.filecollect.common"])
@EnableConfigurationProperties(SecurityProperties::class)
@EnableDiscoveryClient
class GatewayServiceApplication

fun main(args: Array<String>) {
    runApplication<GatewayServiceApplication>(*args)
} 