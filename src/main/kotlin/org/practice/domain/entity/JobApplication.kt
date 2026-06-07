package org.practice.domain.entity

import jakarta.persistence.*
import org.practice.domain.enums.ApplicationSource
import org.practice.domain.enums.ApplicationStatus
import java.time.LocalDate
import java.time.OffsetDateTime

@Entity
@Table(name = "job_applications")
class JobApplication(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(name = "company_name", nullable = false)
    var companyName: String,

    @Column(nullable = false)
    var position: String,

    @Column(name = "job_url")
    var jobUrl: String? = null,

    @Column(name = "salary_min")
    var salaryMin: Int? = null,

    @Column(name = "salary_max")
    var salaryMax: Int? = null,

    @Column(length = 10)
    var currency: String = "RUB",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var source: ApplicationSource = ApplicationSource.OTHER,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: ApplicationStatus = ApplicationStatus.APPLIED,

    @Column(name = "applied_at", nullable = false)
    var appliedAt: LocalDate = LocalDate.now(),

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: OffsetDateTime = OffsetDateTime.now(),

    @OneToMany(mappedBy = "application", cascade = [CascadeType.ALL], orphanRemoval = true)
    val notes: MutableList<ApplicationNote> = mutableListOf(),

    @OneToMany(mappedBy = "application", cascade = [CascadeType.ALL], orphanRemoval = true)
    val reminders: MutableList<Reminder> = mutableListOf(),

    @OneToMany(mappedBy = "application", cascade = [CascadeType.ALL], orphanRemoval = true)
    val statusHistory: MutableList<StatusHistory> = mutableListOf()
)
