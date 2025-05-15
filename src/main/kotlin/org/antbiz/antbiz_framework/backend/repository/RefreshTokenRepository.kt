package org.antbiz.antbiz_framework.backend.repository

import org.antbiz.antbiz_framework.backend.model.RefreshToken
import org.antbiz.antbiz_framework.backend.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Repository
interface RefreshTokenRepository: JpaRepository<RefreshToken, String> {
    fun findByToken(token: String): Optional<RefreshToken>
    fun findByUser(user: User): Optional<RefreshToken>

    @Transactional
    @Modifying
    fun deleteByUser(user: User)
}