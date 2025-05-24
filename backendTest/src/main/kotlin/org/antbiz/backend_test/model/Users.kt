package org.antbiz.backend_test.model

import jakarta.persistence.*
import org.antbiz.antbiz_framework.framework.model.UserEntity

@Entity
@Table(name = "user_additional")
class User(
    id: String? = null,
    username: String,
    email: String,
    password: String,
    roles: String,

    @Column(nullable = true, length = 512)
    open var profile: String? = null,
): UserEntity(
    id, username, email, password, roles,
) {
    constructor(): this(null, "", "", "", "", "")
}
