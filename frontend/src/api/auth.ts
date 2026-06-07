import client from './client'
import type { AuthResponse } from '../types'

export const authApi = {
  register: (email: string, password: string, fullName: string) =>
    client.post<AuthResponse>('/auth/register', { email, password, fullName }).then((r) => r.data),

  login: (email: string, password: string) =>
    client.post<AuthResponse>('/auth/login', { email, password }).then((r) => r.data),
}
