package org.antbiz.backend_test

import io.github.cdimascio.dotenv.dotenv
import org.antbiz.antbiz_framework.framework.EnableAntBizFramework
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


val dotenv = dotenv {
    ignoreIfMissing = true
    filename = "../.env"
}

@SpringBootApplication
@EnableAntBizFramework
class TestApplication

fun main(args: Array<String>) {
    val envVars = mapOf<String, String>(
        "DB_HOST" to dotenv["DB_HOST"],
        "DB_PORT" to dotenv["DB_PORT"],
        "DB_NAME" to dotenv["DB_NAME"],
        "DB_USER" to dotenv["DB_USER"],
        "DB_PASSWORD" to dotenv["DB_PASSWORD"],
        "JWT_SIGNING_KEY" to dotenv["JWT_SIGNING_KEY"],
        "JWT_VALIDATE_KEY" to dotenv["JWT_VALIDATE_KEY"],
        "JWT_ISSUER" to dotenv["JWT_ISSUER"],
        "HOST_NAME" to dotenv["HOST_NAME"],
        "FRONTEND_NAME" to dotenv["FRONTEND_NAME"],
    )

    runApplication<TestApplication>(*args) {
        setDefaultProperties(envVars)
    }
}
