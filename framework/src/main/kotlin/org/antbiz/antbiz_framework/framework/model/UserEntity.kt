package org.antbiz.antbiz_framework.framework.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import space.mori.dalbodeule.snapadmin.external.annotations.DisplayName
import space.mori.dalbodeule.snapadmin.external.annotations.HiddenColumn
import java.io.Serializable
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
open class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) open var id: String? = null,

    @Column(nullable = false, length = 32, name = "username") open var _username: String,

    @Column(nullable = false, length = 255, unique = true) open var email: String,

    @Column(nullable = false, length = 255, name = "password") open var _password: String,

    @Column(nullable = false) open var roles: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) open var gender: GenderEnum,

    @Column(nullable = false) open var birthDate: LocalDateTime,

    @HiddenColumn
    @Column(nullable = true, length = 512) open var profile: String? = null,

    @Column(nullable = false) open val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false) open var updatedAt: LocalDateTime = LocalDateTime.now(),
) : UserDetails, Serializable {
    protected constructor(): this(
        _username = "",
        email = "",
        _password = "",
        roles = "",
        gender = GenderEnum.UNKNOWN,
        birthDate = LocalDateTime.now()
    )
  
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles.split(",").map { role -> SimpleGrantedAuthority(role.trim()) }?.toMutableList()
            ?: mutableListOf(SimpleGrantedAuthority("ROLE_USER"))
    }

    override fun getPassword(): String {
        return _password
    }
    fun setPassword(password: String) {
        this._password = password
    }

    override fun isEnabled(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isAccountNonExpired(): Boolean = true
    override fun getUsername(): String = email

    val age: Long?
        get() = birthDate.let { ChronoUnit.YEARS.between(it, LocalDateTime.now()) }

    @PreUpdate
    private fun onUpdate() {
        updatedAt = LocalDateTime.now()
    }

    @get:DisplayName
    val displayName: String?
        get() = this._username
}

enum class GenderEnum(val value: String) {
    MALE("MALE"),
    FEMALE("FEMALE"),
    THIRD_GENDER("THIRD_GENDER"),
    UNKNOWN("UNKNOWN")
}
