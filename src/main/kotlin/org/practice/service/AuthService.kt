package org.practice.service

import org.practice.domain.entity.User
import org.practice.domain.repository.UserRepository
import org.practice.dto.request.LoginRequest
import org.practice.dto.request.RegisterRequest
import org.practice.dto.response.AuthResponse
import org.practice.exception.ConflictException
import org.practice.security.JwtTokenProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val authenticationManager: AuthenticationManager
) {

    @Transactional
    fun register(request: RegisterRequest): AuthResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw ConflictException("Email already registered: ${request.email}")
        }
        val password = requireNotNull(request.password) { "Password is required" }
        val user = User(
            email = request.email,
            passwordHash = passwordEncoder.encode(password)!!,
            fullName = request.fullName
        )
        userRepository.save(user)
        val token = jwtTokenProvider.generateToken(user.email)
        return AuthResponse(token = token, email = user.email, fullName = user.fullName)
    }

    fun login(request: LoginRequest): AuthResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )
        val user = userRepository.findByEmail(request.email).orElseThrow()
        val token = jwtTokenProvider.generateToken(user.email)
        return AuthResponse(token = token, email = user.email, fullName = user.fullName)
    }
}
