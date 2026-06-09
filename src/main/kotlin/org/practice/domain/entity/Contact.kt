package org.practice.domain.entity

import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
@Table(name = "contacts")
class Contact(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "application_id", nullable = false)
    val application: JobApplication,

    @Column(nullable = false)
    var name: String,

    @Column
    var role: String? = null,

    @Column
    var email: String? = null,

    @Column
    var phone: String? = null,

    @Column(name = "linkedin_url")
    var linkedinUrl: String? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now()
)
