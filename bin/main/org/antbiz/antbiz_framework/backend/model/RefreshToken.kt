package org.antbiz.antbiz_framework.backend.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.io.Serializable
import java.time.LocalDateTime

@Entity

data class RefreshToken(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected var id: String? = null,

    @Column(nullable = false, unique = true, length = 2048)
    protected var token: String,

    @ManyToOne
    @JoinColumn(name = "user_id")
    protected var user: User,

    @Column(nullable = false)
    val expiryDate: LocalDateTime = LocalDateTime.now()
) {
    constructor(): this(
        token = "",
        user = User()
    )
}