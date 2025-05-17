package org.antbiz.antbiz_framework.framework.repository

import org.antbiz.antbiz_framework.framework.model.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional


@Repository
interface UserRepository: JpaRepository<UserEntity, String> {
    fun findByEmail(email: String): Optional<UserEntity>
}