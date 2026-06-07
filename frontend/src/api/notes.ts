import client from './client'
import type { Note } from '../types'

export const notesApi = {
  getAll: (applicationId: number) =>
    client.get<Note[]>(`/applications/${applicationId}/notes`).then((r) => r.data),

  create: (applicationId: number, content: string) =>
    client.post<Note>(`/applications/${applicationId}/notes`, { content }).then((r) => r.data),

  update: (applicationId: number, noteId: number, content: string) =>
    client.put<Note>(`/applications/${applicationId}/notes/${noteId}`, { content }).then((r) => r.data),

  delete: (applicationId: number, noteId: number) =>
    client.delete(`/applications/${applicationId}/notes/${noteId}`),
}
