package org.antbiz.antbiz_framework.framework.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "refresh_token")
data class RefreshToken(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,

    @Column(nullable = false, unique = true, length = 2048)
    var token: String,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: UserEntity?,

    @Column(nullable = false)
    val expiryDate: LocalDateTime = LocalDateTime.now(),
) {
    constructor(): this(
        id = null,
        token = "",
        user = null,
        expiryDate = LocalDateTime.now()
    )
}