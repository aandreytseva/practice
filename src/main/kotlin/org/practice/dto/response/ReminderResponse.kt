package org.practice.dto.response

import org.practice.domain.entity.Reminder
import java.time.OffsetDateTime

data class ReminderResponse(
    val id: Long,
    val applicationId: Long,
    val title: String,
    val remindAt: OffsetDateTime,
    val isDone: Boolean,
    val createdAt: OffsetDateTime
) {
    companion object {
        fun from(reminder: Reminder) = ReminderResponse(
            id = reminder.id,
            applicationId = reminder.application.id,
            title = reminder.title,
            remindAt = reminder.remindAt,
            isDone = reminder.isDone,
            createdAt = reminder.createdAt
        )
    }
}
