package org.practice.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.OffsetDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleNotFound(ex: ResourceNotFoundException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.message ?: "Not found").also {
            it.setProperty("timestamp", OffsetDateTime.now())
        }

    @ExceptionHandler(ConflictException::class)
    fun handleConflict(ex: ConflictException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.message ?: "Conflict").also {
            it.setProperty("timestamp", OffsetDateTime.now())
        }

    @ExceptionHandler(InvalidStatusTransitionException::class)
    fun handleInvalidTransition(ex: InvalidStatusTransitionException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.message ?: "Invalid transition").also {
            it.setProperty("timestamp", OffsetDateTime.now())
        }

    @ExceptionHandler(ForbiddenException::class)
    fun handleForbidden(ex: ForbiddenException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.message ?: "Forbidden").also {
            it.setProperty("timestamp", OffsetDateTime.now())
        }

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentials(ex: BadCredentialsException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Invalid email or password").also {
            it.setProperty("timestamp", OffsetDateTime.now())
        }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ProblemDetail {
        val errors = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "invalid") }
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed").also {
            it.setProperty("timestamp", OffsetDateTime.now())
            it.setProperty("errors", errors)
        }
    }
}
