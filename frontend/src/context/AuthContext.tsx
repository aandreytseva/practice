import { createContext, useContext, useState, useCallback, type ReactNode } from 'react'
import type { AuthResponse } from '../types'

interface AuthUser {
  email: string
  fullName: string
}

interface AuthContextValue {
  user: AuthUser | null
  isAuthenticated: boolean
  login: (data: AuthResponse) => void
  logout: () => void
}

const AuthContext = createContext<AuthContextValue | null>(null)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(() => {
    try {
      const stored = localStorage.getItem('user')
      return stored ? JSON.parse(stored) : null
    } catch {
      return null
    }
  })

  const login = useCallback((data: AuthResponse) => {
    localStorage.setItem('token', data.token)
    const u = { email: data.email, fullName: data.fullName }
    localStorage.setItem('user', JSON.stringify(u))
    setUser(u)
  }, [])

  const logout = useCallback(() => {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    setUser(null)
  }, [])

  return (
    <AuthContext.Provider value={{ user, isAuthenticated: !!user, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used within AuthProvider')
  return ctx
}
