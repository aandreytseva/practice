package org.practice.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.practice.domain.enums.ApplicationSource
import org.practice.domain.enums.ApplicationStatus
import java.time.LocalDate

data class CreateJobApplicationRequest(
    @field:NotBlank val companyName: String,
    @field:NotBlank val position: String,
    val jobUrl: String? = null,
    val salaryMin: Int? = null,
    val salaryMax: Int? = null,
    val currency: String = "RUB",
    val source: ApplicationSource = ApplicationSource.OTHER,
    val appliedAt: LocalDate = LocalDate.now()
)

data class UpdateJobApplicationRequest(
    val companyName: String? = null,
    val position: String? = null,
    val jobUrl: String? = null,
    val salaryMin: Int? = null,
    val salaryMax: Int? = null,
    val currency: String? = null,
    val source: ApplicationSource? = null,
    val appliedAt: LocalDate? = null
)

data class UpdateStatusRequest(
    @field:NotNull val status: ApplicationStatus,
    val comment: String? = null
)
