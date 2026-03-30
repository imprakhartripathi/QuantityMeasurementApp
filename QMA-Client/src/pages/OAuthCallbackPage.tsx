import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/useAuth'

export function OAuthCallbackPage() {
  const navigate = useNavigate()
  const { refreshSession, isAuthenticated } = useAuth()
  const [message, setMessage] = useState('Completing Google authentication and preparing your dashboard.')

  useEffect(() => {
    let mounted = true

    const verifySession = async () => {
      const oauthInProgress = sessionStorage.getItem('qma_oauth_in_progress') === '1'

      if (isAuthenticated && !oauthInProgress) {
        navigate('/dashboard', { replace: true })
        return
      }

      const isExpectedCallback = oauthInProgress
      if (!isExpectedCallback) {
        setMessage('OAuth callback is only valid immediately after starting Google sign-in.')
        setTimeout(() => navigate('/login', { replace: true }), 850)
        return
      }

      const attempts = 5

      for (let attempt = 0; attempt < attempts; attempt += 1) {
        const profile = await refreshSession(attempt === 0)
        if (profile) {
          sessionStorage.removeItem('qma_oauth_in_progress')
          navigate('/dashboard', { replace: true })
          return
        }
        await new Promise((resolve) => setTimeout(resolve, 250))
      }

      if (mounted) {
        sessionStorage.removeItem('qma_oauth_in_progress')
        setMessage('Google sign-in could not establish a session. Please try again.')
        setTimeout(() => {
          navigate('/login', { replace: true })
        }, 900)
      }
    }

    void verifySession()

    return () => {
      mounted = false
    }
  }, [isAuthenticated, navigate, refreshSession])

  return (
    <div className="oauth-status-shell">
      <div className="app-blocking-loader__card oauth-status-card">
        <span className="app-spinner" aria-hidden="true" />
        <div className="oauth-status-copy">
          <h2>Signing you in...</h2>
          <p>{message}</p>
        </div>
      </div>
    </div>
  )
}
