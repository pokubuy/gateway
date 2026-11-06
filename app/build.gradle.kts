import com.google.protobuf.gradle.id
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    java
    alias(libs.plugins.lombok)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.protobuf)
    alias(libs.plugins.spring.dependency.management)
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.spring.boot.starter.webflux)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.grpc.client.starter)
    implementation(libs.reactor.grpc.stub)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.springdoc.webflux.ui)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.asProvider().get().toString()
    }

    plugins {
        id("grpc") {
            artifact = libs.protobuf.protoc.gen.grpc.get().toString()
        }
        id("reactor") {
            artifact = libs.reactor.grpc.asProvider().get().toString()
        }
    }

    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                id("reactor")
            }
        }
    }
}

tasks.named<BootRun>("bootRun") {
    systemProperties["spring.profiles.active"] = "dev"
}