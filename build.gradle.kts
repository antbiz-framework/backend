import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("jvm") version "2.1.10" 
    kotlin("plugin.spring") version "1.9.25" 
    id("org.springframework.boot") version "3.4.5" apply false
    id("io.spring.dependency-management") version "1.1.7" 
    id("org.hibernate.orm") version "6.6.13.Final" 
    kotlin("plugin.jpa") version "1.9.25"
    id("com.vanniktech.maven.publish") version "0.32.0"
    signing
}

group = "org.antbiz.antbiz-framework"
version = "0.0.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://git.mori.space/api/packages/dalbodeule/maven") }
    google()
}

dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}

mavenPublishing {
    coordinates(
        groupId = group as String,
        artifactId = name,
        version = version as String
    )

    pom {
        name = "AntBiz Framework Backend"
        description = "AntBiz Framework Backend starter"
        url = "https://github.com/antbiz-framework/backend"

        licenses {
            license {
                name = "MIT License"
                url = "https://github.com/antbiz-framework/backend/blob/main/LICENSE.md"
            }
        }

        developers {
            developer {
                id = "javaoraclecoffee11"
                name = "javaoraclecoffee11"
                email = "javaoraclecoffee11@gmail.com"
                url = "https://github.com/javaoraclecoffee11"
            }
        }

        scm {
            connection = "scm:git:git://github.com/antbiz-framework/backend.git"
            developerConnection = "scm:git:ssh://github.com/antbiz-framework/backend.git"
            url = "https://github.com/antbiz-framework/backend"
        }
    }

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, true)
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.session:spring-session-core")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    runtimeOnly("org.postgresql:postgresql")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.6")

    implementation("io.swagger.core.v3:swagger-core:2.2.30")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.30")

    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")

    // https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-api
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    // https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-impl
    implementation("io.jsonwebtoken:jjwt-impl:0.12.6")
    // https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-gson
    implementation("io.jsonwebtoken:jjwt-gson:0.12.6")

    implementation("space.mori.dalbodeule:snap-admin:0.5.1")

    implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.2")
    implementation("javax.xml.bind:jaxb-api:2.3.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

hibernate {
    enhancement {
        enableAssociationManagement = true
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
