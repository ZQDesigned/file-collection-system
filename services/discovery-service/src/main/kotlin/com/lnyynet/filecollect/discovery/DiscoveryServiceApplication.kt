package com.lnyynet.filecollect.discovery

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

@SpringBootApplication(scanBasePackages = ["com.lnyynet.filecollect.discovery", "com.lnyynet.filecollect.common"])
@EnableEurekaServer
class DiscoveryServiceApplication

fun main(args: Array<String>) {
    runApplication<DiscoveryServiceApplication>(*args)
} 