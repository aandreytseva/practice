package org.practice.domain.entity

import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
@Table(name = "reminders")
class Reminder(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "application_id", nullable = false)
    val application: JobApplication,

    @Column(nullable = false)
    var title: String,

    @Column(name = "remind_at", nullable = false)
    var remindAt: OffsetDateTime,

    @Column(name = "is_done", nullable = false)
    var isDone: Boolean = false,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now()
)
