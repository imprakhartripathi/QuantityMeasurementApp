import { API_BASE_URL, apiClient } from './apiClient'
import type { AuthResponse, UserProfile } from '../types'

type LoginInput = { email: string; password: string }
type SignupInput = { name: string; email: string; password: string; picture?: string | null }

export const authService = {
  login: (payload: LoginInput) => apiClient.post<AuthResponse>('/api/v1/auth/login', payload),
  signup: (payload: SignupInput) => apiClient.post<AuthResponse>('/api/v1/auth/signup', payload),
  getSession: () => apiClient.get<UserProfile | undefined>('/api/v1/auth/session'),
  logout: () => apiClient.post<void>('/api/v1/auth/logout'),
  startGoogleLogin: () => {
    sessionStorage.setItem('qma_oauth_in_progress', '1')
    const oauthUrl = `${API_BASE_URL}/oauth2/authorization/google`
    window.location.assign(oauthUrl)
  },
}
