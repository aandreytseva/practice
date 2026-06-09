package org.practice.service

import org.practice.domain.entity.Contact
import org.practice.domain.repository.ContactRepository
import org.practice.domain.repository.JobApplicationRepository
import org.practice.domain.repository.UserRepository
import org.practice.dto.request.ContactRequest
import org.practice.dto.response.ContactResponse
import org.practice.exception.ResourceNotFoundException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ContactService(
    private val contactRepository: ContactRepository,
    private val applicationRepository: JobApplicationRepository,
    private val userRepository: UserRepository
) {

    fun getAll(email: String, applicationId: Long): List<ContactResponse> {
        val user = userRepository.findByEmail(email).orElseThrow { UsernameNotFoundException(email) }
        applicationRepository.findByIdAndUserId(applicationId, user.id)
            ?: throw ResourceNotFoundException("Application $applicationId not found")
        return contactRepository.findAllByApplicationId(applicationId).map(ContactResponse::from)
    }

    @Transactional
    fun create(email: String, applicationId: Long, request: ContactRequest): ContactResponse {
        val user = userRepository.findByEmail(email).orElseThrow { UsernameNotFoundException(email) }
        val app = applicationRepository.findByIdAndUserId(applicationId, user.id)
            ?: throw ResourceNotFoundException("Application $applicationId not found")
        val contact = contactRepository.save(
            Contact(
                application = app,
                name = request.name,
                role = request.role,
                email = request.email,
                phone = request.phone,
                linkedinUrl = request.linkedinUrl
            )
        )
        return ContactResponse.from(contact)
    }

    @Transactional
    fun delete(email: String, applicationId: Long, contactId: Long) {
        val user = userRepository.findByEmail(email).orElseThrow { UsernameNotFoundException(email) }
        applicationRepository.findByIdAndUserId(applicationId, user.id)
            ?: throw ResourceNotFoundException("Application $applicationId not found")
        val contact = contactRepository.findByIdAndApplicationId(contactId, applicationId)
            ?: throw ResourceNotFoundException("Contact $contactId not found")
        contactRepository.delete(contact)
    }
}
