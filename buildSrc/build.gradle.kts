plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22")
    implementation("org.jetbrains.kotlin:kotlin-allopen:1.9.22")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:3.2.3")
    implementation("io.spring.gradle:dependency-management-plugin:1.1.4")
}
