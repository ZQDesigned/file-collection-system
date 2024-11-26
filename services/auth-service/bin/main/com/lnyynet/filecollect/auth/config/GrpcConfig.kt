package com.lnyynet.filecollect.auth.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import net.devh.boot.grpc.server.autoconfigure.GrpcServerAutoConfiguration
import net.devh.boot.grpc.server.autoconfigure.GrpcServerFactoryAutoConfiguration

@Configuration
@Import(
    GrpcServerAutoConfiguration::class,
    GrpcServerFactoryAutoConfiguration::class
)
class GrpcConfig 