package org.practice.domain.repository

import org.practice.domain.entity.JobApplication
import org.practice.domain.enums.ApplicationSource
import org.practice.domain.enums.ApplicationStatus
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDate

object JobApplicationSpec {

    fun forUser(userId: Long): Specification<JobApplication> =
        Specification { root, _, cb -> cb.equal(root.get<Any>("user").get<Long>("id"), userId) }

    fun search(query: String): Specification<JobApplication> =
        Specification { root, _, cb ->
            val pattern = "%${query.lowercase()}%"
            cb.or(
                cb.like(cb.lower(root.get("companyName")), pattern),
                cb.like(cb.lower(root.get("position")), pattern)
            )
        }

    fun hasStatus(status: ApplicationStatus): Specification<JobApplication> =
        Specification { root, _, cb -> cb.equal(root.get<String>("status"), status.name) }

    fun hasSource(source: ApplicationSource): Specification<JobApplication> =
        Specification { root, _, cb -> cb.equal(root.get<String>("source"), source.name) }

    fun salaryMinAtLeast(min: Int): Specification<JobApplication> =
        Specification { root, _, cb -> cb.greaterThanOrEqualTo(root.get("salaryMin"), min) }

    fun salaryMaxAtMost(max: Int): Specification<JobApplication> =
        Specification { root, _, cb -> cb.lessThanOrEqualTo(root.get("salaryMax"), max) }

    fun appliedFrom(date: LocalDate): Specification<JobApplication> =
        Specification { root, _, cb -> cb.greaterThanOrEqualTo(root.get("appliedAt"), date) }

    fun appliedTo(date: LocalDate): Specification<JobApplication> =
        Specification { root, _, cb -> cb.lessThanOrEqualTo(root.get("appliedAt"), date) }
}
