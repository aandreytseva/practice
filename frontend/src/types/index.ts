export type ApplicationStatus =
  | 'APPLIED'
  | 'HR_CALL'
  | 'TECH_INTERVIEW'
  | 'FINAL_INTERVIEW'
  | 'OFFER'
  | 'REJECTED'
  | 'WITHDRAWN'

export type ApplicationSource =
  | 'HH_RU'
  | 'LINKEDIN'
  | 'HABR_CAREER'
  | 'COMPANY_WEBSITE'
  | 'REFERRAL'
  | 'OTHER'

export const STATUS_TRANSITIONS: Record<ApplicationStatus, ApplicationStatus[]> = {
  APPLIED:         ['HR_CALL', 'TECH_INTERVIEW', 'REJECTED', 'WITHDRAWN'],
  HR_CALL:         ['TECH_INTERVIEW', 'REJECTED', 'WITHDRAWN'],
  TECH_INTERVIEW:  ['FINAL_INTERVIEW', 'OFFER', 'REJECTED', 'WITHDRAWN'],
  FINAL_INTERVIEW: ['OFFER', 'REJECTED', 'WITHDRAWN'],
  OFFER:           ['WITHDRAWN'],
  REJECTED:        [],
  WITHDRAWN:       [],
}

export const STATUS_LABELS: Record<ApplicationStatus, string> = {
  APPLIED:         'Applied',
  HR_CALL:         'HR Call',
  TECH_INTERVIEW:  'Tech Interview',
  FINAL_INTERVIEW: 'Final Interview',
  OFFER:           'Offer',
  REJECTED:        'Rejected',
  WITHDRAWN:       'Withdrawn',
}

export const STATUS_COLORS: Record<ApplicationStatus, string> = {
  APPLIED:         'bg-blue-100 text-blue-800',
  HR_CALL:         'bg-purple-100 text-purple-800',
  TECH_INTERVIEW:  'bg-orange-100 text-orange-800',
  FINAL_INTERVIEW: 'bg-yellow-100 text-yellow-800',
  OFFER:           'bg-green-100 text-green-800',
  REJECTED:        'bg-red-100 text-red-800',
  WITHDRAWN:       'bg-gray-100 text-gray-600',
}

export const SOURCE_LABELS: Record<ApplicationSource, string> = {
  HH_RU:            'hh.ru',
  LINKEDIN:         'LinkedIn',
  HABR_CAREER:      'Habr Career',
  COMPANY_WEBSITE:  'Company Website',
  REFERRAL:         'Referral',
  OTHER:            'Other',
}

export const ALL_STATUSES: ApplicationStatus[] = [
  'APPLIED', 'HR_CALL', 'TECH_INTERVIEW', 'FINAL_INTERVIEW', 'OFFER', 'REJECTED', 'WITHDRAWN',
]

// --- API response types ---

export interface JobApplication {
  id: number
  companyName: string
  position: string
  jobUrl: string | null
  salaryMin: number | null
  salaryMax: number | null
  currency: string
  source: ApplicationSource
  status: ApplicationStatus
  appliedAt: string
  createdAt: string
  updatedAt: string
}

export interface Page<T> {
  content: T[]
  totalElements: number
  totalPages: number
  number: number
  size: number
}

export interface StatusHistoryEntry {
  id: number
  fromStatus: ApplicationStatus | null
  toStatus: ApplicationStatus
  comment: string | null
  changedAt: string
}

export interface Note {
  id: number
  content: string
  createdAt: string
  updatedAt: string
}

export interface Reminder {
  id: number
  applicationId: number
  title: string
  remindAt: string
  isDone: boolean
  createdAt: string
}

export interface StatsResponse {
  total: number
  byStatus: Partial<Record<ApplicationStatus, number>>
}

export interface AuthResponse {
  token: string
  email: string
  fullName: string
}
