package org.antbiz.antbiz_framework.framework.service

import org.antbiz.antbiz_framework.framework.jwt.JwtProvider
import org.antbiz.antbiz_framework.framework.model.RefreshToken
import org.antbiz.antbiz_framework.framework.model.UserEntity
import org.antbiz.antbiz_framework.framework.repository.RefreshTokenRepository
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

@Service
class TokenService(
    @Autowired private val jwtProvider: JwtProvider,
    @Autowired private val refreshTokenRepository: RefreshTokenRepository,
    @Value("\${antbiz.jwt.refresh-token-validity}") private val refreshTokenValidity: Long
) {
    fun generateAccessToken(user: UserEntity): String {
        return jwtProvider.generateAccessToken(user)
    }

    fun generateRefreshToken(user: UserEntity): String {
        val refreshToken = jwtProvider.generateRefreshToken(user)
        val newRefreshToken = RefreshToken(
            token = refreshToken,
            user = user,
            expiryDate = LocalDateTime.now().plusSeconds(refreshTokenValidity)
        )

        refreshTokenRepository.findByUser(user).getOrNull()?.let {
            refreshTokenRepository.delete(it)
        }
        refreshTokenRepository.save(newRefreshToken)
        return refreshToken
    }

    @Transactional
    fun verifyExpiration(token: RefreshToken): RefreshToken {
        if(token.expiryDate.isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token)
            throw RuntimeException("Refresh token was expired.")
        }

        return token
    }

    fun findByToken(token: String): Optional<RefreshToken> {
        return refreshTokenRepository.findByToken(token)
    }

    //@Modifying
    @Transactional
    fun deleteByUser(user: UserEntity) {
        refreshTokenRepository.deleteByUser(user)
    }

    fun getRefreshTokenValidity(): Long {
        return refreshTokenValidity
    }
}