package org.practice.dto.response

data class AuthResponse(
    val token: String,
    val email: String,
    val fullName: String
)
