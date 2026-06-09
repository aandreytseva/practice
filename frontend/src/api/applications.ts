import client from './client'
import type { JobApplication, Page, ApplicationStatus, ApplicationSource, StatusHistoryEntry, StatsResponse } from '../types'

export interface CreateApplicationPayload {
  companyName: string
  position: string
  jobUrl?: string
  salaryMin?: number
  salaryMax?: number
  currency?: string
  source: ApplicationSource
  appliedAt?: string
}

export interface UpdateApplicationPayload extends Partial<CreateApplicationPayload> {}

export const applicationsApi = {
  getAll: (params?: {
    search?: string
    status?: ApplicationStatus
    source?: string
    salaryMin?: number
    salaryMax?: number
    appliedFrom?: string
    appliedTo?: string
    page?: number
    size?: number
  }) =>
    client.get<Page<JobApplication>>('/applications', { params }).then((r) => r.data),

  getById: (id: number) =>
    client.get<JobApplication>(`/applications/${id}`).then((r) => r.data),

  create: (payload: CreateApplicationPayload) =>
    client.post<JobApplication>('/applications', payload).then((r) => r.data),

  update: (id: number, payload: UpdateApplicationPayload) =>
    client.patch<JobApplication>(`/applications/${id}`, payload).then((r) => r.data),

  updateStatus: (id: number, status: ApplicationStatus, comment?: string) =>
    client.patch<JobApplication>(`/applications/${id}/status`, { status, comment }).then((r) => r.data),

  delete: (id: number) =>
    client.delete(`/applications/${id}`),

  getHistory: (id: number) =>
    client.get<StatusHistoryEntry[]>(`/applications/${id}/history`).then((r) => r.data),

  getStats: () =>
    client.get<StatsResponse>('/applications/stats').then((r) => r.data),
}
