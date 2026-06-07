package org.practice.controller

import jakarta.validation.Valid
import org.practice.dto.request.ReminderRequest
import org.practice.dto.response.ReminderResponse
import org.practice.service.ReminderService
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/applications/{applicationId}/reminders")
class ReminderController(private val reminderService: ReminderService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @AuthenticationPrincipal user: UserDetails,
        @PathVariable applicationId: Long,
        @Valid @RequestBody request: ReminderRequest
    ): ReminderResponse = reminderService.create(user.username, applicationId, request)

    @GetMapping
    fun getAll(
        @AuthenticationPrincipal user: UserDetails,
        @PathVariable applicationId: Long
    ): List<ReminderResponse> = reminderService.getAll(user.username, applicationId)

    @PatchMapping("/{reminderId}/done")
    fun markDone(
        @AuthenticationPrincipal user: UserDetails,
        @PathVariable applicationId: Long,
        @PathVariable reminderId: Long
    ): ReminderResponse = reminderService.markDone(user.username, applicationId, reminderId)

    @DeleteMapping("/{reminderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @AuthenticationPrincipal user: UserDetails,
        @PathVariable applicationId: Long,
        @PathVariable reminderId: Long
    ) = reminderService.delete(user.username, applicationId, reminderId)
}
