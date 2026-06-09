package org.practice.service

import org.practice.domain.entity.JobApplication
import org.practice.domain.entity.StatusHistory
import org.practice.domain.repository.JobApplicationRepository
import org.practice.domain.repository.JobApplicationSpec
import org.practice.domain.repository.StatusHistoryRepository
import org.practice.domain.repository.UserRepository
import org.practice.dto.request.CreateJobApplicationRequest
import org.practice.dto.request.UpdateJobApplicationRequest
import org.practice.dto.request.UpdateStatusRequest
import org.practice.dto.response.JobApplicationResponse
import org.practice.dto.response.StatsResponse
import org.practice.dto.response.StatusHistoryResponse
import org.practice.domain.enums.ApplicationSource
import org.practice.domain.enums.ApplicationStatus
import org.practice.exception.InvalidStatusTransitionException
import org.practice.exception.ResourceNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.OffsetDateTime

@Service
class JobApplicationService(
    private val jobApplicationRepository: JobApplicationRepository,
    private val statusHistoryRepository: StatusHistoryRepository,
    private val userRepository: UserRepository
) {

    @Transactional
    fun create(email: String, request: CreateJobApplicationRequest): JobApplicationResponse {
        val user = userRepository.findByEmail(email)
            .orElseThrow { UsernameNotFoundException(email) }

        val application = JobApplication(
            user = user,
            companyName = request.companyName,
            position = request.position,
            jobUrl = request.jobUrl,
            salaryMin = request.salaryMin,
            salaryMax = request.salaryMax,
            currency = request.currency,
            source = request.source,
            appliedAt = request.appliedAt
        )
        jobApplicationRepository.save(application)

        statusHistoryRepository.save(
            StatusHistory(
                application = application,
                fromStatus = null,
                toStatus = ApplicationStatus.APPLIED
            )
        )

        return JobApplicationResponse.from(application)
    }

    @Transactional(readOnly = true)
    fun getAll(
        email: String,
        search: String?,
        status: ApplicationStatus?,
        source: ApplicationSource?,
        salaryMin: Int?,
        salaryMax: Int?,
        appliedFrom: LocalDate?,
        appliedTo: LocalDate?,
        pageable: Pageable
    ): Page<JobApplicationResponse> {
        val user = userRepository.findByEmail(email).orElseThrow { UsernameNotFoundException(email) }

        var spec: Specification<JobApplication> = JobApplicationSpec.forUser(user.id)
        if (!search.isNullOrBlank()) spec = spec.and(JobApplicationSpec.search(search))
        if (status != null)          spec = spec.and(JobApplicationSpec.hasStatus(status))
        if (source != null)          spec = spec.and(JobApplicationSpec.hasSource(source))
        if (salaryMin != null)       spec = spec.and(JobApplicationSpec.salaryMinAtLeast(salaryMin))
        if (salaryMax != null)       spec = spec.and(JobApplicationSpec.salaryMaxAtMost(salaryMax))
        if (appliedFrom != null)     spec = spec.and(JobApplicationSpec.appliedFrom(appliedFrom))
        if (appliedTo != null)       spec = spec.and(JobApplicationSpec.appliedTo(appliedTo))

        return jobApplicationRepository.findAll(spec, pageable).map { JobApplicationResponse.from(it) }
    }

    @Transactional(readOnly = true)
    fun getById(email: String, id: Long): JobApplicationResponse {
        val user = userRepository.findByEmail(email).orElseThrow { UsernameNotFoundException(email) }
        val app = jobApplicationRepository.findByIdAndUserId(id, user.id)
            ?: throw ResourceNotFoundException("Application $id not found")
        return JobApplicationResponse.from(app)
    }

    @Transactional
    fun update(email: String, id: Long, request: UpdateJobApplicationRequest): JobApplicationResponse {
        val user = userRepository.findByEmail(email).orElseThrow { UsernameNotFoundException(email) }
        val app = jobApplicationRepository.findByIdAndUserId(id, user.id)
            ?: throw ResourceNotFoundException("Application $id not found")

        request.companyName?.let { app.companyName = it }
        request.position?.let { app.position = it }
        request.jobUrl?.let { app.jobUrl = it }
        request.salaryMin?.let { app.salaryMin = it }
        request.salaryMax?.let { app.salaryMax = it }
        request.currency?.let { app.currency = it }
        request.source?.let { app.source = it }
        request.appliedAt?.let { app.appliedAt = it }
        app.updatedAt = OffsetDateTime.now()

        return JobApplicationResponse.from(jobApplicationRepository.save(app))
    }

    @Transactional
    fun updateStatus(email: String, id: Long, request: UpdateStatusRequest): JobApplicationResponse {
        val user = userRepository.findByEmail(email).orElseThrow { UsernameNotFoundException(email) }
        val app = jobApplicationRepository.findByIdAndUserId(id, user.id)
            ?: throw ResourceNotFoundException("Application $id not found")

        if (!app.status.canTransitionTo(request.status)) {
            throw InvalidStatusTransitionException(
                "Cannot transition from ${app.status} to ${request.status}"
            )
        }

        val previousStatus = app.status
        app.status = request.status
        app.updatedAt = OffsetDateTime.now()
        jobApplicationRepository.save(app)

        statusHistoryRepository.save(
            StatusHistory(
                application = app,
                fromStatus = previousStatus,
                toStatus = request.status,
                comment = request.comment
            )
        )

        return JobApplicationResponse.from(app)
    }

    @Transactional
    fun delete(email: String, id: Long) {
        val user = userRepository.findByEmail(email).orElseThrow { UsernameNotFoundException(email) }
        val app = jobApplicationRepository.findByIdAndUserId(id, user.id)
            ?: throw ResourceNotFoundException("Application $id not found")
        jobApplicationRepository.delete(app)
    }

    @Transactional(readOnly = true)
    fun getStatusHistory(email: String, id: Long): List<StatusHistoryResponse> {
        val user = userRepository.findByEmail(email).orElseThrow { UsernameNotFoundException(email) }
        jobApplicationRepository.findByIdAndUserId(id, user.id)
            ?: throw ResourceNotFoundException("Application $id not found")

        return statusHistoryRepository.findByApplicationIdOrderByChangedAtAsc(id).map {
            StatusHistoryResponse(
                id = it.id,
                fromStatus = it.fromStatus,
                toStatus = it.toStatus,
                comment = it.comment,
                changedAt = it.changedAt
            )
        }
    }

    @Transactional(readOnly = true)
    fun getStats(email: String): StatsResponse {
        val user = userRepository.findByEmail(email).orElseThrow { UsernameNotFoundException(email) }
        val rows = jobApplicationRepository.countByUserIdGroupByStatus(user.id)
        val byStatus = rows.associate { row ->
            (row[0] as ApplicationStatus) to (row[1] as Long)
        }
        val total = byStatus.values.sum()
        return StatsResponse(total = total, byStatus = byStatus)
    }
}
