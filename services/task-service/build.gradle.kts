plugins {
    id("common-conventions")
}

dependencies {
    implementation(project(":services:common"))
    
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    
    // Spring Cloud
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    
    // 数据库
    implementation("org.postgresql:postgresql")
    
    // gRPC
    implementation(Deps.grpcSpringBootStarterClient)
    
    // 监控
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")
    
    // 日志
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")
    
    // Kotlin
    implementation(Deps.kotlinCoroutines)
    implementation(Deps.kotlinCoroutinesReactor)
}
