package com.lnyynet.filecollect.file

import net.devh.boot.grpc.server.autoconfigure.GrpcServerSecurityAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication(
    scanBasePackages = ["com.lnyynet.filecollect.file", "com.lnyynet.filecollect.common"],
    exclude = [GrpcServerSecurityAutoConfiguration::class]
)
@EnableDiscoveryClient
class FileServiceApplication

fun main(args: Array<String>) {
    runApplication<FileServiceApplication>(*args)
} 