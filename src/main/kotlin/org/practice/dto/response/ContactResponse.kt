package org.practice.dto.response

import org.practice.domain.entity.Contact
import java.time.OffsetDateTime

data class ContactResponse(
    val id: Long,
    val name: String,
    val role: String?,
    val email: String?,
    val phone: String?,
    val linkedinUrl: String?,
    val createdAt: OffsetDateTime
) {
    companion object {
        fun from(c: Contact) = ContactResponse(
            id = c.id,
            name = c.name,
            role = c.role,
            email = c.email,
            phone = c.phone,
            linkedinUrl = c.linkedinUrl,
            createdAt = c.createdAt
        )
    }
}
