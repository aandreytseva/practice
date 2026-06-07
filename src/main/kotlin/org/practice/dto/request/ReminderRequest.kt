package org.practice.dto.request

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.OffsetDateTime

data class ReminderRequest(
    @field:NotBlank val title: String,
    @field:NotNull @field:Future val remindAt: OffsetDateTime
)
