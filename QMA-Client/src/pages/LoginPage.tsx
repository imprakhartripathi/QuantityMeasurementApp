import { useEffect, useState } from 'react'
import { Link, Navigate, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/useAuth'
import { Sparkles } from 'lucide-react'
import '../styles/auth.scss'

export function LoginPage() {
  const { isAuthenticated, login } = useAuth()
  const navigate = useNavigate()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [submitting, setSubmitting] = useState(false)

  useEffect(() => {
    if (!isAuthenticated) {
      return
    }
    navigate('/dashboard', { replace: true })
  }, [isAuthenticated, navigate])

  if (isAuthenticated) {
    return <Navigate to="/dashboard" replace />
  }

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    setError('')
    setSubmitting(true)
    try {
      await login(email, password)
    } catch (loginError) {
      setError(loginError instanceof Error ? loginError.message : 'Login failed')
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="auth-page">
      <aside className="auth-hero">
        <div className="auth-hero__badge">
          <Sparkles size={16} />
          <span>Quantity Measurement</span>
        </div>
        <h1>Work with units in one secure, clean workspace.</h1>
        <p>
          Compare, convert and calculate across length, weight, volume and temperature with full operation history.
        </p>
      </aside>

      <main className="auth-main">
        <div className="auth-card">
          <h2>Welcome back</h2>
          <p className="auth-subtitle">Login to continue to your dashboard.</p>

          <form className="auth-form" onSubmit={handleSubmit}>
            <label className="auth-field">
              <span>Email</span>
              <input type="email" required value={email} onChange={(event) => setEmail(event.target.value)} />
            </label>

            <label className="auth-field">
              <span>Password</span>
              <input
                type="password"
                required
                value={password}
                onChange={(event) => setPassword(event.target.value)}
              />
            </label>

            {error ? <p className="auth-error">{error}</p> : null}

            <button type="submit" disabled={submitting} className="auth-btn auth-btn--primary">
              {submitting ? 'Signing in...' : 'Login'}
            </button>
          </form>

          {/* Temporarily disabled OAuth entrypoint
          <button type="button" onClick={startGoogleLogin} className="auth-btn auth-btn--google">
            Continue with Google
          </button>
          */}

          <p className="auth-footer">
            New here? <Link to="/signup">Create account</Link>
          </p>
        </div>
      </main>
    </div>
  )
}
