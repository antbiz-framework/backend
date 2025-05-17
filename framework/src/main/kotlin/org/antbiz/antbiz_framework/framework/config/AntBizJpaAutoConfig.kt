package org.antbiz.antbiz_framework.framework.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(AntBizProperties::class, JwtProperties::class, EtcProperties::class)
class AntBizJpaAutoConfig

@ConfigurationProperties(prefix = "antbiz")
data class AntBizProperties(
    val jwt: JwtProperties = JwtProperties(),
    val etc: EtcProperties = EtcProperties(),
)

@ConfigurationProperties(prefix = "antbiz.jwt")
data class JwtProperties(
    val signingKey: String = "",
    val validateKey: String = "",
    val issuer: String = "",
    val accessTokenValidity: Long = 900,
    val refreshTokenValidity: Long = 2592000,
)

@ConfigurationProperties(prefix = "antbiz.etc")
data class EtcProperties(
    val host: String = "",
    val frontend: String = "",
)

