import { useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export function OAuthCallbackPage() {
  const navigate = useNavigate()
  const { refreshSession } = useAuth()

  useEffect(() => {
    const verifySession = async () => {
      await refreshSession(true)
      navigate('/dashboard', { replace: true })
    }
    void verifySession()
  }, [navigate, refreshSession])

  return (
    <div className="flex min-h-screen items-center justify-center px-4">
      <div className="content-panel max-w-md px-6 py-10 text-center">
        <h2 className="text-xl font-semibold text-slate-900">Signing you in...</h2>
        <p className="mt-2 text-sm text-slate-500">Completing Google authentication and preparing your dashboard.</p>
      </div>
    </div>
  )
}
