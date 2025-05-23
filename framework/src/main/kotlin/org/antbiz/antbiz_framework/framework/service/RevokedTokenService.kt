package org.antbiz.antbiz_framework.framework.service

import org.antbiz.antbiz_framework.framework.model.RevokedToken
import org.antbiz.antbiz_framework.framework.repository.RevokedTokenRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RevokedTokenService(
    @Autowired
    private val revokedTokenRepository: RevokedTokenRepository,
) {
    // 토큰이 Revoke되었는지 확인
    fun isTokenRevoked(token: String): Boolean {
        return revokedTokenRepository.findByToken(token).isPresent
    }

    // 토큰을 Revoke 처리
    fun revokeToken(token: String) {
        if (!isTokenRevoked(token)) {
            val revokedToken = RevokedToken(token = token)
            revokedTokenRepository.save(revokedToken)
        }
    }
}