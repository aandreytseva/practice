package org.practice.domain.repository

import org.practice.domain.entity.JobApplication
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query

interface JobApplicationRepository : JpaRepository<JobApplication, Long>,
    JpaSpecificationExecutor<JobApplication> {

    fun findByIdAndUserId(id: Long, userId: Long): JobApplication?

    fun findByUserId(userId: Long, pageable: Pageable): Page<JobApplication>

    @Query("""
        SELECT ja.status, COUNT(ja)
        FROM JobApplication ja
        WHERE ja.user.id = :userId
        GROUP BY ja.status
    """)
    fun countByUserIdGroupByStatus(userId: Long): List<Array<Any>>
}
