package org.practice.controller

import jakarta.validation.Valid
import org.practice.dto.request.LoginRequest
import org.practice.dto.request.RegisterRequest
import org.practice.dto.response.AuthResponse
import org.practice.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@Valid @RequestBody request: RegisterRequest): AuthResponse =
        authService.register(request)

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): AuthResponse =
        authService.login(request)
}
