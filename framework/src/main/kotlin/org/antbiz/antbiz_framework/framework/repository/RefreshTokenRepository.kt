package org.antbiz.antbiz_framework.framework.repository

import org.antbiz.antbiz_framework.framework.model.RefreshToken
import org.antbiz.antbiz_framework.framework.model.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional


@Repository
interface RefreshTokenRepository: JpaRepository<RefreshToken, String> {
    fun findByToken(token: String): Optional<RefreshToken>
    fun findByUser(user: UserEntity): Optional<RefreshToken>

    @Transactional
    @Modifying
    fun deleteByUser(user: UserEntity)
}
