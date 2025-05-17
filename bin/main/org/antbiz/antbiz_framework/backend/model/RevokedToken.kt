package org.antbiz.antbiz_framework.backend.model

import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@Table(name = "revoked_token")
data class RevokedToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true, length = 2048)
    var token: String,

    @Column(name = "revoked_at", nullable = false)
    val revokedAt: LocalDateTime = LocalDateTime.now()
) {
    constructor(): this(token = "")
}