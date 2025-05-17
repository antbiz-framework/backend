package org.antbiz.antbiz_framework.backend.repository

import org.antbiz.antbiz_framework.backend.model.RevokedToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface RevokedTokenRepository : JpaRepository<RevokedToken, Long> {
    fun findByToken(token: String): Optional<RevokedToken> // 토큰으로 조회
}