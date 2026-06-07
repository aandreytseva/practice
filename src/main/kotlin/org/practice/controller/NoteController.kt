package org.practice.controller

import jakarta.validation.Valid
import org.practice.dto.request.NoteRequest
import org.practice.dto.response.NoteResponse
import org.practice.service.NoteService
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/applications/{applicationId}/notes")
class NoteController(private val noteService: NoteService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @AuthenticationPrincipal user: UserDetails,
        @PathVariable applicationId: Long,
        @Valid @RequestBody request: NoteRequest
    ): NoteResponse = noteService.create(user.username, applicationId, request)

    @GetMapping
    fun getAll(
        @AuthenticationPrincipal user: UserDetails,
        @PathVariable applicationId: Long
    ): List<NoteResponse> = noteService.getAll(user.username, applicationId)

    @PutMapping("/{noteId}")
    fun update(
        @AuthenticationPrincipal user: UserDetails,
        @PathVariable applicationId: Long,
        @PathVariable noteId: Long,
        @Valid @RequestBody request: NoteRequest
    ): NoteResponse = noteService.update(user.username, applicationId, noteId, request)

    @DeleteMapping("/{noteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @AuthenticationPrincipal user: UserDetails,
        @PathVariable applicationId: Long,
        @PathVariable noteId: Long
    ) = noteService.delete(user.username, applicationId, noteId)
}
