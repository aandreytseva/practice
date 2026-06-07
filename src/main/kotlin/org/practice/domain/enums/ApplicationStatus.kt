package org.practice.domain.enums

enum class ApplicationStatus {
    APPLIED,
    HR_CALL,
    TECH_INTERVIEW,
    FINAL_INTERVIEW,
    OFFER,
    REJECTED,
    WITHDRAWN;

    fun allowedTransitions(): Set<ApplicationStatus> = when (this) {
        APPLIED          -> setOf(HR_CALL, TECH_INTERVIEW, REJECTED, WITHDRAWN)
        HR_CALL          -> setOf(TECH_INTERVIEW, REJECTED, WITHDRAWN)
        TECH_INTERVIEW   -> setOf(FINAL_INTERVIEW, OFFER, REJECTED, WITHDRAWN)
        FINAL_INTERVIEW  -> setOf(OFFER, REJECTED, WITHDRAWN)
        OFFER            -> setOf(WITHDRAWN)
        REJECTED         -> emptySet()
        WITHDRAWN        -> emptySet()
    }

    fun canTransitionTo(next: ApplicationStatus): Boolean = next in allowedTransitions()
}
