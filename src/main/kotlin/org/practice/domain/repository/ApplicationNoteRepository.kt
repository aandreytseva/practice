package org.practice.domain.repository

import org.practice.domain.entity.ApplicationNote
import org.springframework.data.jpa.repository.JpaRepository

interface ApplicationNoteRepository : JpaRepository<ApplicationNote, Long> {
    fun findByApplicationIdOrderByCreatedAtDesc(applicationId: Long): List<ApplicationNote>
    fun findByIdAndApplicationId(id: Long, applicationId: Long): ApplicationNote?
}
