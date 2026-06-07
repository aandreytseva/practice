package org.practice.dto.request

import jakarta.validation.constraints.NotBlank

data class NoteRequest(
    @field:NotBlank val content: String
)
