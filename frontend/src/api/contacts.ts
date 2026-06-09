import client from './client'

export interface Contact {
  id: number
  name: string
  role: string | null
  email: string | null
  phone: string | null
  linkedinUrl: string | null
  createdAt: string
}

export interface CreateContactPayload {
  name: string
  role?: string
  email?: string
  phone?: string
  linkedinUrl?: string
}

export const contactsApi = {
  getAll: (applicationId: number) =>
    client.get<Contact[]>(`/applications/${applicationId}/contacts`).then((r) => r.data),

  create: (applicationId: number, payload: CreateContactPayload) =>
    client.post<Contact>(`/applications/${applicationId}/contacts`, payload).then((r) => r.data),

  delete: (applicationId: number, contactId: number) =>
    client.delete(`/applications/${applicationId}/contacts/${contactId}`),
}
