package org.practice.config

import org.practice.security.JwtAuthFilter
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.practice.security.JwtProperties
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JwtProperties::class)
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter,
    private val userDetailsService: UserDetailsService
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .csrf { it.disable() }
            .cors { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/api/auth/**").permitAll()
                    .anyRequest().authenticated()
            }
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()

    // Prevent Spring Boot from auto-registering JwtAuthFilter as a standalone servlet filter
    @Bean
    fun jwtFilterRegistration(jwtAuthFilter: JwtAuthFilter): FilterRegistrationBean<JwtAuthFilter> =
        FilterRegistrationBean(jwtAuthFilter).apply { isEnabled = false }

    @Bean
    fun authenticationProvider(): AuthenticationProvider =
        DaoAuthenticationProvider(userDetailsService).apply {
            setPasswordEncoder(passwordEncoder())
        }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager =
        config.authenticationManager

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
