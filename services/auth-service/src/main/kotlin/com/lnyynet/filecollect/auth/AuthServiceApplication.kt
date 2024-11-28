package com.lnyynet.filecollect.auth

import net.devh.boot.grpc.server.autoconfigure.GrpcServerSecurityAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication(
    scanBasePackages = ["com.lnyynet.filecollect.auth", "com.lnyynet.filecollect.common"],
    exclude = [GrpcServerSecurityAutoConfiguration::class]
)
@EnableDiscoveryClient
class AuthServiceApplication

fun main(args: Array<String>) {
    runApplication<AuthServiceApplication>(*args)
}