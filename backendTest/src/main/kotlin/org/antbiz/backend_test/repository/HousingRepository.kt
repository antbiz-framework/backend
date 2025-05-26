package org.antbiz.backend_test.repository

import org.antbiz.backend_test.model.Housing
import org.springframework.data.jpa.repository.JpaRepository

interface HousingRepository: JpaRepository<Housing, Long> {
}