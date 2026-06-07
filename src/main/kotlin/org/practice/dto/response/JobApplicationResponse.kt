package org.practice.dto.response

import org.practice.domain.entity.JobApplication
import org.practice.domain.enums.ApplicationSource
import org.practice.domain.enums.ApplicationStatus
import java.time.LocalDate
import java.time.OffsetDateTime

data class JobApplicationResponse(
    val id: Long,
    val companyName: String,
    val position: String,
    val jobUrl: String?,
    val salaryMin: Int?,
    val salaryMax: Int?,
    val currency: String,
    val source: ApplicationSource,
    val status: ApplicationStatus,
    val appliedAt: LocalDate,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
) {
    companion object {
        fun from(app: JobApplication) = JobApplicationResponse(
            id = app.id,
            companyName = app.companyName,
            position = app.position,
            jobUrl = app.jobUrl,
            salaryMin = app.salaryMin,
            salaryMax = app.salaryMax,
            currency = app.currency,
            source = app.source,
            status = app.status,
            appliedAt = app.appliedAt,
            createdAt = app.createdAt,
            updatedAt = app.updatedAt
        )
    }
}

data class StatusHistoryResponse(
    val id: Long,
    val fromStatus: ApplicationStatus?,
    val toStatus: ApplicationStatus,
    val comment: String?,
    val changedAt: OffsetDateTime
)

data class StatsResponse(
    val total: Long,
    val byStatus: Map<ApplicationStatus, Long>
)
