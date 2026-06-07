import client from './client'
import type { Reminder } from '../types'

export const remindersApi = {
  getAll: (applicationId: number) =>
    client.get<Reminder[]>(`/applications/${applicationId}/reminders`).then((r) => r.data),

  create: (applicationId: number, title: string, remindAt: string) =>
    client.post<Reminder>(`/applications/${applicationId}/reminders`, { title, remindAt }).then((r) => r.data),

  markDone: (applicationId: number, reminderId: number) =>
    client.patch<Reminder>(`/applications/${applicationId}/reminders/${reminderId}/done`).then((r) => r.data),

  delete: (applicationId: number, reminderId: number) =>
    client.delete(`/applications/${applicationId}/reminders/${reminderId}`),
}
