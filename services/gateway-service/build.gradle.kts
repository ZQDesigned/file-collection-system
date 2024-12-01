plugins {
    id("common-conventions")
}

dependencies {
    implementation(project(":services:common"))
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    
    // Spring Cloud Gateway
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation("io.grpc:grpc-netty:1.68.2")
    
    // OpenAPI
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:${Versions.springDoc}")
    
    // 限流
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    
    // 认证
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:${Versions.jwt}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${Versions.jwt}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${Versions.jwt}")
    
    // 监控
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")
    
    // 日志
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")
    
    // Kotlin
    implementation(Deps.kotlinCoroutines)
    implementation(Deps.kotlinCoroutinesReactor)
} 