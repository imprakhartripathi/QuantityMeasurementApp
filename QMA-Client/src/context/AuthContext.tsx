import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react'
import { authService } from '../services/authService'
import type { UserProfile } from '../types'

type AuthContextValue = {
  user: UserProfile | null
  loading: boolean
  isAuthenticated: boolean
  refreshSession: () => Promise<void>
  login: (email: string, password: string) => Promise<void>
  signup: (name: string, email: string, password: string, picture?: string) => Promise<void>
  logout: () => Promise<void>
  startGoogleLogin: () => void
  setUser: (user: UserProfile | null) => void
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined)

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<UserProfile | null>(null)
  const [loading, setLoading] = useState(true)

  const refreshSession = useCallback(async () => {
    try {
      const profile = await authService.getSession()
      setUser(profile)
    } catch {
      setUser(null)
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    void refreshSession()
  }, [refreshSession])

  const login = useCallback(
    async (email: string, password: string) => {
      const response = await authService.login({ email, password })
      setUser(response.user)
    },
    [],
  )

  const signup = useCallback(
    async (name: string, email: string, password: string, picture?: string) => {
      const response = await authService.signup({ name, email, password, picture: picture || null })
      setUser(response.user)
    },
    [],
  )

  const logout = useCallback(async () => {
    await authService.logout()
    setUser(null)
  }, [])

  const value = useMemo<AuthContextValue>(
    () => ({
      user,
      loading,
      isAuthenticated: Boolean(user),
      refreshSession,
      login,
      signup,
      logout,
      startGoogleLogin: authService.startGoogleLogin,
      setUser,
    }),
    [user, loading, refreshSession, login, signup, logout],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth must be used inside AuthProvider')
  }
  return context
}
