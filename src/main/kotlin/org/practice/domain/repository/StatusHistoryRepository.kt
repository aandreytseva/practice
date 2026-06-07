package org.practice.domain.repository

import org.practice.domain.entity.StatusHistory
import org.springframework.data.jpa.repository.JpaRepository

interface StatusHistoryRepository : JpaRepository<StatusHistory, Long> {
    fun findByApplicationIdOrderByChangedAtAsc(applicationId: Long): List<StatusHistory>
}
