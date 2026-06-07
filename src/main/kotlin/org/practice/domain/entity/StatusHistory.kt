package org.practice.domain.entity

import jakarta.persistence.*
import org.practice.domain.enums.ApplicationStatus
import java.time.OffsetDateTime

@Entity
@Table(name = "status_history")
class StatusHistory(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "application_id", nullable = false)
    val application: JobApplication,

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status")
    val fromStatus: ApplicationStatus? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", nullable = false)
    val toStatus: ApplicationStatus,

    @Column
    val comment: String? = null,

    @Column(name = "changed_at", nullable = false, updatable = false)
    val changedAt: OffsetDateTime = OffsetDateTime.now()
)
