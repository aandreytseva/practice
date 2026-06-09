package org.practice.dto.request

import jakarta.validation.constraints.NotBlank

data class ContactRequest(
    @field:NotBlank val name: String,
    val role: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val linkedinUrl: String? = null
)
