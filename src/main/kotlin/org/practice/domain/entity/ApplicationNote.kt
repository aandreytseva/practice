package org.practice.domain.entity

import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
@Table(name = "application_notes")
class ApplicationNote(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "application_id", nullable = false)
    val application: JobApplication,

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: OffsetDateTime = OffsetDateTime.now()
)
