import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.w3c.dom.DOMImplementation

plugins {
    id("org.springframework.boot") version "3.1.2"
    id("io.spring.dependency-management") version "1.1.2"
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.spring") version "1.9.0"
    kotlin("plugin.noarg") version "1.9.20"
    kotlin("plugin.jpa") version "1.9.0"
    kotlin("kapt") version "1.9.0"
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

noArg {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

group = "io.directional"
version = "0.0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    // spring boot
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // database
    runtimeOnly("com.h2database:h2")

    // querydsl
    implementation("com.querydsl:querydsl-core:5.0.0")
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // restAssured
    testImplementation("io.rest-assured:kotlin-extensions:5.0.1")

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
