plugins {
    id("common-conventions")
}

dependencies {
    implementation(project(":services:common"))
    
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    
    // Spring Cloud
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    
    // 数据库
    implementation("org.postgresql:postgresql")
    
    // JWT
    implementation("io.jsonwebtoken:jjwt-api:${Versions.jwt}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${Versions.jwt}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${Versions.jwt}")
    
    // gRPC
    implementation(Deps.grpcSpringBootStarter)
    
    // 监控
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")
    
    // 日志
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")
}
