package org.antbiz.backend_test.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "housing")
data class Housing(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var longitude: Double,

    @Column(nullable = false)
    var latitude: Double,

    @Column(nullable = false)
    var housingMedianAge: Double,

    @Column(nullable = false)
    var totalRooms: Double,

    @Column(nullable = false)
    var totalBedrooms: Double,

    @Column(nullable = false)
    var population: Double,

    @Column(nullable = false)
    var households: Double,

    @Column(nullable = false)
    var medianIncome: Double,

    @Column(nullable = false)
    var medianHouseValue: Double,

    @Column(nullable = false, length = 32)
    var oceanProximity: String,

    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),
) {
    constructor(): this(
        null,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        ""
    )

    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }
}