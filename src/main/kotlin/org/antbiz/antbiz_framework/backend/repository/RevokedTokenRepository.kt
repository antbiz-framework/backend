package org.antbiz.antbiz_framework.backend.repository

import org.antbiz.antbiz_framework.backend.model.RevokedToken
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.context.annotation.ImportRuntimeHints
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional


class RevokedTokenHints : RuntimeHintsRegistrar {
    override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
        hints.reflection().registerType(RevokedToken::class.java)
    }
}

@Repository
@ImportRuntimeHints(RevokedTokenHints::class)
interface RevokedTokenRepository : JpaRepository<RevokedToken, Long> {
    fun findByToken(token: String): Optional<RevokedToken> // 토큰으로 조회
}