package org.practice.controller

import jakarta.validation.Valid
import org.practice.domain.enums.ApplicationStatus
import org.practice.dto.request.CreateJobApplicationRequest
import org.practice.dto.request.UpdateJobApplicationRequest
import org.practice.dto.request.UpdateStatusRequest
import org.practice.dto.response.JobApplicationResponse
import org.practice.dto.response.StatsResponse
import org.practice.dto.response.StatusHistoryResponse
import org.practice.service.JobApplicationService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/applications")
class JobApplicationController(private val service: JobApplicationService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @AuthenticationPrincipal user: UserDetails,
        @Valid @RequestBody request: CreateJobApplicationRequest
    ): JobApplicationResponse = service.create(user.username, request)

    @GetMapping
    fun getAll(
        @AuthenticationPrincipal user: UserDetails,
        @RequestParam(required = false) status: ApplicationStatus?,
        @PageableDefault(size = 20, sort = ["createdAt"]) pageable: Pageable
    ): Page<JobApplicationResponse> = service.getAll(user.username, status, pageable)

    @GetMapping("/{id}")
    fun getById(
        @AuthenticationPrincipal user: UserDetails,
        @PathVariable id: Long
    ): JobApplicationResponse = service.getById(user.username, id)

    @PatchMapping("/{id}")
    fun update(
        @AuthenticationPrincipal user: UserDetails,
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateJobApplicationRequest
    ): JobApplicationResponse = service.update(user.username, id, request)

    @PatchMapping("/{id}/status")
    fun updateStatus(
        @AuthenticationPrincipal user: UserDetails,
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateStatusRequest
    ): JobApplicationResponse = service.updateStatus(user.username, id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @AuthenticationPrincipal user: UserDetails,
        @PathVariable id: Long
    ) = service.delete(user.username, id)

    @GetMapping("/{id}/history")
    fun getHistory(
        @AuthenticationPrincipal user: UserDetails,
        @PathVariable id: Long
    ): List<StatusHistoryResponse> = service.getStatusHistory(user.username, id)

    @GetMapping("/stats")
    fun getStats(@AuthenticationPrincipal user: UserDetails): StatsResponse =
        service.getStats(user.username)
}
