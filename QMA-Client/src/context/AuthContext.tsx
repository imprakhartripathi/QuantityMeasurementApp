import { useCallback, useEffect, useMemo, useState } from 'react'
import { authService } from '../services/authService'
import type { UserProfile } from '../types'
import { AuthContext, type AuthContextValue } from './auth-context'

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<UserProfile | null>(null)
  const [loading, setLoading] = useState(true)
  const [isLoggingOut, setIsLoggingOut] = useState(false)

  const refreshSession = useCallback(async (showLoader = false) => {
    if (showLoader) {
      setLoading(true)
    }
    try {
      const profile = await authService.getSession()
      const nextUser = profile ?? null
      setUser(nextUser)
      return nextUser
    } catch {
      setUser(null)
      return null
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
      sessionStorage.removeItem('qma_oauth_in_progress')
      await refreshSession(true)
    },
    [refreshSession],
  )

  const signup = useCallback(
    async (name: string, email: string, password: string, picture?: string) => {
      await authService.signup({ name, email, password, picture: picture || null })
      sessionStorage.removeItem('qma_oauth_in_progress')
      await refreshSession(true)
    },
    [refreshSession],
  )

  const logout = useCallback(async () => {
    setIsLoggingOut(true)
    try {
      await Promise.race([
        authService.logout(),
        new Promise<void>((resolve) => {
          window.setTimeout(resolve, 1800)
        }),
      ])
    } finally {
      sessionStorage.removeItem('qma_oauth_in_progress')
      localStorage.removeItem('qma_oauth_result')
      if ('caches' in window) {
        try {
          void window.caches.keys().then((keys) => Promise.all(keys.map((key) => window.caches.delete(key))))
        } catch {
          // no-op
        }
      }
      setUser(null)
      setIsLoggingOut(false)
    }
  }, [])

  const value = useMemo<AuthContextValue>(
    () => ({
      user,
      loading,
      isLoggingOut,
      isAuthenticated: Boolean(user),
      refreshSession,
      login,
      signup,
      logout,
      startGoogleLogin: authService.startGoogleLogin,
      setUser,
    }),
    [user, loading, isLoggingOut, refreshSession, login, signup, logout],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}
