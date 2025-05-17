package org.antbiz.antbiz_framework.framework.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig(
    @Value("\${antbiz.etc.host}")
    private val host: String,
    @Value("\${antbiz.etc.frontend}")
    private val frontend: String,
) {
    private val securitySchemeName = "api token"

    val hosts = "$host, $frontend"
    val servers = hosts.split(",").mapNotNull { it.trim() }.map { Server().url(it) }

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(Info().title("AntBiz Framework API Server").version("v1.0.0"))
            .servers(servers)
            .addSecurityItem(SecurityRequirement().addList(securitySchemeName))
            .components(Components()
                .addSecuritySchemes(securitySchemeName,
                    SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                )
            )
    }
}