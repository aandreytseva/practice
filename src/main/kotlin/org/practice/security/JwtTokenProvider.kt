package org.practice.security

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(private val jwtProperties: JwtProperties) {

    private val secretKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray())
    }

    fun generateToken(email: String): String =
        Jwts.builder()
            .subject(email)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + jwtProperties.expirationMs))
            .signWith(secretKey)
            .compact()

    fun extractEmail(token: String): String =
        Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
            .subject

    fun isTokenValid(token: String): Boolean =
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
            true
        } catch (e: JwtException) {
            false
        } catch (e: IllegalArgumentException) {
            false
        }
}
