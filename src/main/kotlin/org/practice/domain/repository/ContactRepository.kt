package org.practice.domain.repository

import org.practice.domain.entity.Contact
import org.springframework.data.jpa.repository.JpaRepository

interface ContactRepository : JpaRepository<Contact, Long> {
    fun findAllByApplicationId(applicationId: Long): List<Contact>
    fun findByIdAndApplicationId(id: Long, applicationId: Long): Contact?
}
