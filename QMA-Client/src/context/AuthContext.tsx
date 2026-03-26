import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react'
import { authService } from '../services/authService'
import type { UserProfile } from '../types'

type AuthContextValue = {
  user: UserProfile | null
  loading: boolean
  isAuthenticated: boolean
  refreshSession: (showLoader?: boolean) => Promise<void>
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

  const refreshSession = useCallback(async (showLoader = false) => {
    if (showLoader) {
      setLoading(true)
    }
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
      await authService.login({ email, password })
      await refreshSession(true)
    },
    [refreshSession],
  )

  const signup = useCallback(
    async (name: string, email: string, password: string, picture?: string) => {
      await authService.signup({ name, email, password, picture: picture || null })
      await refreshSession(true)
    },
    [refreshSession],
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
