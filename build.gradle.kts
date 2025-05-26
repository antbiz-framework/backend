plugins {
    kotlin("jvm") version "2.1.10" apply false
    kotlin("plugin.spring") version "1.9.25" apply false
    id("org.springframework.boot") version "3.4.5" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    id("org.hibernate.orm") version "6.6.13.Final" apply false
    id("org.graalvm.buildtools.native") version "0.10.6" apply false
    kotlin("plugin.jpa") version "1.9.25" apply false
    id("com.github.node-gradle.node") version "7.1.0" apply false
}

allprojects {
    group = "com.example"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    if (name == "backendTest") {
        apply(plugin = "org.jetbrains.kotlin.jvm")
        apply(plugin = "org.jetbrains.kotlin.plugin.spring")
        apply(plugin = "org.springframework.boot")
        apply(plugin = "io.spring.dependency-management")
        apply(plugin = "org.hibernate.orm")
        apply(plugin = "org.graalvm.buildtools.native")
        apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
    }
    if (name == "frontendTest") {
        apply(plugin = "com.github.node-gradle.node")
    }
}
