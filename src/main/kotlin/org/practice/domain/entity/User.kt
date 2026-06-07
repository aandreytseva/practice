package org.practice.domain.entity

import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
@Table(name = "users")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(name = "password_hash", nullable = false)
    var passwordHash: String,

    @Column(name = "full_name", nullable = false)
    var fullName: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: OffsetDateTime = OffsetDateTime.now(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val applications: MutableList<JobApplication> = mutableListOf()
)
