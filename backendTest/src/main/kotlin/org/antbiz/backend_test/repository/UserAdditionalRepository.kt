package org.antbiz.backend_test.repository

import org.antbiz.backend_test.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserAdditionalRepository: JpaRepository<User, String> {
    fun findByEmail(email: String): Optional<User>
}