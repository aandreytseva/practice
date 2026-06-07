package org.practice.dto.response

import org.practice.domain.entity.ApplicationNote
import java.time.OffsetDateTime

data class NoteResponse(
    val id: Long,
    val content: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
) {
    companion object {
        fun from(note: ApplicationNote) = NoteResponse(
            id = note.id,
            content = note.content,
            createdAt = note.createdAt,
            updatedAt = note.updatedAt
        )
    }
}
