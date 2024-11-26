plugins {
    id("common-conventions")
}

dependencies {
    implementation(project(":services:common"))
    
    // Eureka Server
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server")
    
    // 监控
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")
    
    // 日志
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")
} 