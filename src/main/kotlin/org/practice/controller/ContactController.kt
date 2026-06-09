package org.practice.controller

import jakarta.validation.Valid
import org.practice.dto.request.ContactRequest
import org.practice.dto.response.ContactResponse
import org.practice.service.ContactService
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/applications/{applicationId}/contacts")
class ContactController(private val service: ContactService) {

    @GetMapping
    fun getAll(
        @AuthenticationPrincipal user: UserDetails,
        @PathVariable applicationId: Long
    ): List<ContactResponse> = service.getAll(user.username, applicationId)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @AuthenticationPrincipal user: UserDetails,
        @PathVariable applicationId: Long,
        @Valid @RequestBody request: ContactRequest
    ): ContactResponse = service.create(user.username, applicationId, request)

    @DeleteMapping("/{contactId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @AuthenticationPrincipal user: UserDetails,
        @PathVariable applicationId: Long,
        @PathVariable contactId: Long
    ) = service.delete(user.username, applicationId, contactId)
}
