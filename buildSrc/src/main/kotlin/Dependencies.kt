object Versions {
    const val springDoc = "2.3.0"
    const val postgresql = "42.7.2"
    const val minio = "8.5.8"
    const val jwt = "0.12.5"
    const val rabbitmq = "5.20.0"
    const val grpc = "1.61.1"
    const val protobuf = "3.25.2"
    const val grpcKotlin = "1.4.1"
    const val grpcSpringBoot = "2.15.0.RELEASE"
    const val coroutines = "1.7.3"
    const val springCloud = "2023.0.0"
    const val springCloudGateway = "4.1.1"
    const val springCloudNetflix = "4.1.0"
}

object Deps {
    // Spring 相关
    const val springBootWeb = "org.springframework.boot:spring-boot-starter-web"
    const val springBootSecurity = "org.springframework.boot:spring-boot-starter-security"
    const val springBootJpa = "org.springframework.boot:spring-boot-starter-data-jpa"
    const val springBootValidation = "org.springframework.boot:spring-boot-starter-validation"
    const val springBootAmqp = "org.springframework.boot:spring-boot-starter-amqp"
    
    // 数据库
    const val postgresql = "org.postgresql:postgresql:${Versions.postgresql}"
    
    // 文档
    const val springDoc = "org.springdoc:springdoc-openapi-starter-webmvc-ui:${Versions.springDoc}"
    
    // 存储
    const val minio = "io.minio:minio:${Versions.minio}"
    
    // 安全
    const val jjwt = "io.jsonwebtoken:jjwt:${Versions.jwt}"
    
    // 消息队列
    const val rabbitmq = "com.rabbitmq:amqp-client:${Versions.rabbitmq}"
    
    // gRPC
    const val grpcSpringBootStarter = "net.devh:grpc-server-spring-boot-starter:${Versions.grpcSpringBoot}"
    const val grpcSpringBootStarterClient = "net.devh:grpc-client-spring-boot-starter:${Versions.grpcSpringBoot}"
    const val grpcProtobuf = "io.grpc:grpc-protobuf:${Versions.grpc}"
    const val grpcStub = "io.grpc:grpc-stub:${Versions.grpc}"
    const val grpcKotlin = "io.grpc:grpc-kotlin-stub:${Versions.grpcKotlin}"
    const val protobuf = "com.google.protobuf:protobuf-kotlin:${Versions.protobuf}"
    
    // Kotlin
    const val kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val kotlinCoroutinesReactor = "org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${Versions.coroutines}"
    
    // Spring Cloud
    const val springCloudGateway = "org.springframework.cloud:spring-cloud-starter-gateway:${Versions.springCloudGateway}"
    const val springCloudNetflix = "org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:${Versions.springCloudNetflix}"
}
