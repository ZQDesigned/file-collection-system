plugins {
    id("common-conventions")
    id("com.google.protobuf") version "0.9.4"
}

dependencies {
    api(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-validation")
    api("org.springdoc:springdoc-openapi-starter-webmvc-ui:${Versions.springDoc}")
    api("com.fasterxml.jackson.module:jackson-module-kotlin")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    api("jakarta.persistence:jakarta.persistence-api:3.1.0")
    api("org.jetbrains.kotlin:kotlin-reflect")
    
    // gRPC
    api(Deps.grpcProtobuf)
    api(Deps.grpcStub)
    api(Deps.grpcKotlin)
    api(Deps.protobuf)
    
    // Kotlin
    api(Deps.kotlinCoroutines)
    api(Deps.kotlinCoroutinesReactor)
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${Versions.protobuf}"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${Versions.grpc}"
        }
        create("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${Versions.grpcKotlin}:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                create("grpc")
                create("grpckt")
            }
            it.builtins {
                create("kotlin")
            }
        }
    }
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
