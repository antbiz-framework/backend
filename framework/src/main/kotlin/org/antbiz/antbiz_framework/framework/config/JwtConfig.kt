package org.antbiz.antbiz_framework.framework.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

@Configuration
class JwtConfig(
    @Value("\${antbiz.jwt.signing-key}") private val privateKeyBase64: String,
    @Value("\${antbiz.jwt.validate-key}") private val publicKeyBase64: String
) {

    @Bean
    fun privateKey(): PrivateKey {
        try {
            val keyData = privateKeyBase64
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("\\s".toRegex(), "") // Remove newlines and extra spaces

            val keyBytes = Base64.getDecoder().decode(keyData)
            val keySpec = PKCS8EncodedKeySpec(keyBytes)
            val keyFactory = KeyFactory.getInstance("RSA")
            return keyFactory.generatePrivate(keySpec)
        } catch(ex: Exception) {
            throw RuntimeException("Invalid private key", ex)
        }
    }

    @Bean
    fun publicKey(): PublicKey {
        try {
            val keyData = publicKeyBase64
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("\\s".toRegex(), "") // Remove newlines and extra spaces

            val keyBytes = Base64.getDecoder().decode(keyData)
            val keySpec = X509EncodedKeySpec(keyBytes)
            val keyFactory = KeyFactory.getInstance("RSA")
            return keyFactory.generatePublic(keySpec)
        } catch(ex: Exception) {
            throw RuntimeException("Invalid public key", ex)
        }
    }
}