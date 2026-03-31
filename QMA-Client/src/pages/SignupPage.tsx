import { useEffect, useState } from 'react'
import { Link, Navigate, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/useAuth'
import { UserRoundPlus } from 'lucide-react'
import '../styles/auth.scss'

export function SignupPage() {
  const { isAuthenticated, signup } = useAuth()
  const navigate = useNavigate()
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [picture, setPicture] = useState('')
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
      await signup(name, email, password, picture)
    } catch (signupError) {
      setError(signupError instanceof Error ? signupError.message : 'Signup failed')
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="auth-page">
      <aside className="auth-hero auth-hero--signup">
        <div className="auth-hero__badge">
          <UserRoundPlus size={16} />
          <span>Create account</span>
        </div>
        <h1>Build your personal workspace in under a minute.</h1>
        <p>Use manual signup and access your full quantity operation history instantly.</p>
      </aside>

      <main className="auth-main">
        <div className="auth-card">
          <h2>Create account</h2>
          <p className="auth-subtitle">Set up your profile and start measuring.</p>

          <form className="auth-form" onSubmit={handleSubmit}>
            <label className="auth-field">
              <span>Name</span>
              <input
                type="text"
                required
                value={name}
                onChange={(event) => setName(event.target.value)}
                placeholder="Your full name"
              />
            </label>

            <label className="auth-field">
              <span>Email</span>
              <input
                type="email"
                required
                value={email}
                onChange={(event) => setEmail(event.target.value)}
                placeholder="you@example.com"
              />
            </label>

            <label className="auth-field">
              <span>Password</span>
              <input
                type="password"
                required
                minLength={8}
                value={password}
                onChange={(event) => setPassword(event.target.value)}
                placeholder="At least 8 characters"
              />
            </label>

            <label className="auth-field">
              <span>Picture URL (optional)</span>
              <input
                type="url"
                value={picture}
                onChange={(event) => setPicture(event.target.value)}
                placeholder="https://example.com/avatar.png"
              />
            </label>

            {error ? <p className="auth-error">{error}</p> : null}

            <button type="submit" disabled={submitting} className="auth-btn auth-btn--primary">
              {submitting ? 'Creating account...' : 'Create account'}
            </button>
          </form>

          {/* Temporarily disabled OAuth entrypoint
          <button type="button" onClick={startGoogleLogin} className="auth-btn auth-btn--google">
            Continue with Google
          </button>
          */}

          <p className="auth-footer">
            Already have an account? <Link to="/login">Login</Link>
          </p>
        </div>
      </main>
    </div>
  )
}
