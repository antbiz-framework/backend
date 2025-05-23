package org.antbiz.backend_test.model

import jakarta.persistence.*
import org.antbiz.antbiz_framework.framework.model.GenderEnum
import org.antbiz.antbiz_framework.framework.model.UserEntity
import java.time.LocalDateTime

@Entity
@Table(name = "user_additional")
open class UserAdditional(
    id: String? = null,
    username: String,
    email: String,
    password: String,
    roles: String,
    gender: GenderEnum,
    birthDate: LocalDateTime,
    profile: String? = null,

    @Column(nullable = false, length = PHONE_LENGTH, unique = true)
    var phoneNumber: String,

    @Column(nullable = false, length = ADDRESS_LENGTH, unique = true)
    var address: String
) : UserEntity(
    id = id,
    _username = username,
    email = email,
    _password = password,
    roles = roles,
    gender = gender,
    birthDate = birthDate,
    profile = profile
) {
    companion object {
        private const val PHONE_LENGTH = 128
        private const val ADDRESS_LENGTH = 128
    }

    constructor() : this(
        username = "",
        email = "",
        password = "",
        roles = "",
        gender = GenderEnum.UNKNOWN,
        birthDate = LocalDateTime.now(),
        profile = null,
        phoneNumber = "",
        address = ""
    )
}