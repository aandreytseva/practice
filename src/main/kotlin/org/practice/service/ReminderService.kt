package org.practice.service

import org.practice.domain.entity.Reminder
import org.practice.domain.repository.JobApplicationRepository
import org.practice.domain.repository.ReminderRepository
import org.practice.domain.repository.UserRepository
import org.practice.dto.request.ReminderRequest
import org.practice.dto.response.ReminderResponse
import org.practice.exception.ResourceNotFoundException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReminderService(
    private val reminderRepository: ReminderRepository,
    private val applicationRepository: JobApplicationRepository,
    private val userRepository: UserRepository
) {

    private fun resolveApplication(email: String, applicationId: Long) =
        applicationRepository.findByIdAndUserId(
            applicationId,
            userRepository.findByEmail(email).orElseThrow { UsernameNotFoundException(email) }.id
        ) ?: throw ResourceNotFoundException("Application $applicationId not found")

    @Transactional
    fun create(email: String, applicationId: Long, request: ReminderRequest): ReminderResponse {
        val app = resolveApplication(email, applicationId)
        val reminder = reminderRepository.save(
            Reminder(application = app, title = request.title, remindAt = request.remindAt)
        )
        return ReminderResponse.from(reminder)
    }

    @Transactional(readOnly = true)
    fun getAll(email: String, applicationId: Long): List<ReminderResponse> {
        resolveApplication(email, applicationId)
        return reminderRepository.findByApplicationIdOrderByRemindAtAsc(applicationId).map { ReminderResponse.from(it) }
    }

    @Transactional
    fun markDone(email: String, applicationId: Long, reminderId: Long): ReminderResponse {
        resolveApplication(email, applicationId)
        val reminder = reminderRepository.findByIdAndApplicationId(reminderId, applicationId)
            ?: throw ResourceNotFoundException("Reminder $reminderId not found")
        reminder.isDone = true
        return ReminderResponse.from(reminderRepository.save(reminder))
    }

    @Transactional
    fun delete(email: String, applicationId: Long, reminderId: Long) {
        resolveApplication(email, applicationId)
        val reminder = reminderRepository.findByIdAndApplicationId(reminderId, applicationId)
            ?: throw ResourceNotFoundException("Reminder $reminderId not found")
        reminderRepository.delete(reminder)
    }
}
