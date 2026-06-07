package org.practice.domain.repository

import org.practice.domain.entity.Reminder
import org.springframework.data.jpa.repository.JpaRepository
import java.time.OffsetDateTime

interface ReminderRepository : JpaRepository<Reminder, Long> {
    fun findByApplicationIdOrderByRemindAtAsc(applicationId: Long): List<Reminder>
    fun findByIdAndApplicationId(id: Long, applicationId: Long): Reminder?
    fun findByIsDoneFalseAndRemindAtBefore(dateTime: OffsetDateTime): List<Reminder>
}
