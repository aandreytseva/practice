package org.practice.service

import org.practice.domain.entity.ApplicationNote
import org.practice.domain.repository.ApplicationNoteRepository
import org.practice.domain.repository.JobApplicationRepository
import org.practice.domain.repository.UserRepository
import org.practice.dto.request.NoteRequest
import org.practice.dto.response.NoteResponse
import org.practice.exception.ResourceNotFoundException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Service
class NoteService(
    private val noteRepository: ApplicationNoteRepository,
    private val applicationRepository: JobApplicationRepository,
    private val userRepository: UserRepository
) {

    private fun resolveApplication(email: String, applicationId: Long) =
        applicationRepository.findByIdAndUserId(
            applicationId,
            userRepository.findByEmail(email).orElseThrow { UsernameNotFoundException(email) }.id
        ) ?: throw ResourceNotFoundException("Application $applicationId not found")

    @Transactional
    fun create(email: String, applicationId: Long, request: NoteRequest): NoteResponse {
        val app = resolveApplication(email, applicationId)
        val note = noteRepository.save(ApplicationNote(application = app, content = request.content))
        return NoteResponse.from(note)
    }

    @Transactional(readOnly = true)
    fun getAll(email: String, applicationId: Long): List<NoteResponse> {
        resolveApplication(email, applicationId)
        return noteRepository.findByApplicationIdOrderByCreatedAtDesc(applicationId).map { NoteResponse.from(it) }
    }

    @Transactional
    fun update(email: String, applicationId: Long, noteId: Long, request: NoteRequest): NoteResponse {
        resolveApplication(email, applicationId)
        val note = noteRepository.findByIdAndApplicationId(noteId, applicationId)
            ?: throw ResourceNotFoundException("Note $noteId not found")
        note.content = request.content
        note.updatedAt = OffsetDateTime.now()
        return NoteResponse.from(noteRepository.save(note))
    }

    @Transactional
    fun delete(email: String, applicationId: Long, noteId: Long) {
        resolveApplication(email, applicationId)
        val note = noteRepository.findByIdAndApplicationId(noteId, applicationId)
            ?: throw ResourceNotFoundException("Note $noteId not found")
        noteRepository.delete(note)
    }
}
