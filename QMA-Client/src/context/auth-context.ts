import { createContext } from 'react'
import type { UserProfile } from '../types'

export type AuthContextValue = {
  user: UserProfile | null
  loading: boolean
  isLoggingOut: boolean
  isAuthenticated: boolean
  refreshSession: (showLoader?: boolean) => Promise<UserProfile | null>
  login: (email: string, password: string) => Promise<void>
  signup: (name: string, email: string, password: string, picture?: string) => Promise<void>
  logout: () => Promise<void>
  startGoogleLogin: () => void
  setUser: (user: UserProfile | null) => void
}

export const AuthContext = createContext<AuthContextValue | undefined>(undefined)
