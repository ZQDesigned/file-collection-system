rootProject.name = "file-collect"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    plugins {
        kotlin("jvm") version "1.9.22"
        kotlin("plugin.spring") version "1.9.22"
        id("org.springframework.boot") version "3.2.3"
        id("io.spring.dependency-management") version "1.1.4"
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(
    "services:common",
    "services:auth-service",
    "services:file-service",
    "services:task-service",
    "services:notification-service",
    "services:gateway-service",
    "services:discovery-service"
)