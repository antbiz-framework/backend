package org.antbiz.backend_test

import io.github.cdimascio.dotenv.dotenv
import org.antbiz.antbiz_framework.framework.EnableAntBizFramework
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories


val dotenv = dotenv {
    ignoreIfMissing = true
    filename = "../.env"
}

@SpringBootApplication
@EnableAntBizFramework
@EnableJpaRepositories(basePackages = ["org.antbiz.backend_test.repository"])
@EntityScan(basePackages = ["org.antbiz.backend_test.model"])
class TestApplication

fun main(args: Array<String>) {
    val envVars = mapOf<String, String>(
        "DB_HOST" to dotenv["ANTBIZ_DB_HOST"],
        "DB_PORT" to dotenv["ANTBIZ_DB_PORT"],
        "DB_NAME" to dotenv["ANTBIZ_DB_NAME"],
        "DB_USER" to dotenv["ANTBIZ_DB_USER"],
        "DB_PASSWORD" to dotenv["ANTBIZ_DB_PASSWORD"],
        "JWT_SIGNING_KEY" to dotenv["ANTBIZ_JWT_SIGNING_KEY"],
        "JWT_VALIDATE_KEY" to dotenv["ANTBIZ_JWT_VALIDATE_KEY"],
        "JWT_ISSUER" to dotenv["ANTBIZ_JWT_ISSUER"],
        "HOST_NAME" to dotenv["ANTBIZ_HOST_NAME"],
        "FRONTEND_NAME" to dotenv["ANTBIZ_FRONTEND_NAME"],
    )

    runApplication<TestApplication>(*args) {
        setDefaultProperties(envVars)
    }
}
