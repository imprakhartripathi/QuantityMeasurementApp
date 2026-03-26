import { API_BASE_URL, apiClient } from './apiClient'
import type { AuthResponse, UserProfile } from '../types'

type LoginInput = { email: string; password: string }
type SignupInput = { name: string; email: string; password: string; picture?: string | null }

export const authService = {
  login: (payload: LoginInput) => apiClient.post<AuthResponse>('/api/v1/auth/login', payload),
  signup: (payload: SignupInput) => apiClient.post<AuthResponse>('/api/v1/auth/signup', payload),
  getSession: () => apiClient.get<UserProfile>('/api/v1/auth/session'),
  logout: () => apiClient.post<void>('/api/v1/auth/logout'),
  startGoogleLogin: () => {
    window.location.href = `${API_BASE_URL}/oauth2/authorization/google`
  },
}
