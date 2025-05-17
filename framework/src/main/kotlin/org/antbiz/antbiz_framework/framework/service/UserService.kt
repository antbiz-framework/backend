package org.antbiz.antbiz_framework.framework.service

import org.antbiz.antbiz_framework.framework.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserService(
    @Autowired private val userRepository: UserRepository,
): UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        return userRepository.findByEmail(email)
            .orElseThrow { UsernameNotFoundException("User $email not found") }
    }

    fun loadUserById(userId: String): UserDetails {
        return userRepository.findById(userId)
            .orElseThrow { UsernameNotFoundException("UserId $userId not found") }
    }
}