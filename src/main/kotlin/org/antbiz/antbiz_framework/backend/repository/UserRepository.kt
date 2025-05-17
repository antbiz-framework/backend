package org.antbiz.antbiz_framework.backend.repository

import org.antbiz.antbiz_framework.backend.model.User
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.context.annotation.ImportRuntimeHints
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional


class UserHints : RuntimeHintsRegistrar {
    override fun registerHints(hints: org.springframework.aot.hint.RuntimeHints, classLoader: java.lang.ClassLoader?) {
        hints.reflection().registerType(User::class.java)
    }
}

@Repository
@ImportRuntimeHints(UserHints::class)
interface UserRepository: JpaRepository<User, String> {
    fun findByEmail(email: String): Optional<User>
}