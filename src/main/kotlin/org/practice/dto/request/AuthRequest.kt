package org.practice.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegisterRequest(
    @field:Email val email: String,
    @field:NotBlank @field:Size(min = 6) val password: String,
    @field:NotBlank val fullName: String
)

data class LoginRequest(
    @field:Email val email: String,
    @field:NotBlank val password: String
)
