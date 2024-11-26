package com.lnyynet.filecollect.task.config

import net.devh.boot.grpc.client.autoconfigure.GrpcClientAutoConfiguration
import net.devh.boot.grpc.client.autoconfigure.GrpcClientSecurityAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    GrpcClientAutoConfiguration::class,
    GrpcClientSecurityAutoConfiguration::class
)
class AuthClientConfig 