import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    java
    alias(libs.plugins.lombok)
    alias(libs.plugins.spring.boot)
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

    implementation(project(":user-service:api"))
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.named<BootRun>("bootRun") {
    systemProperties["spring.profiles.active"] = "dev"
}