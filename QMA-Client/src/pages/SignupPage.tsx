import { useEffect, useState } from 'react'
import { Link, Navigate, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export function SignupPage() {
  const { isAuthenticated, signup, startGoogleLogin } = useAuth()
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
      navigate('/dashboard', { replace: true })
    } catch (signupError) {
      setError(signupError instanceof Error ? signupError.message : 'Signup failed')
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="flex min-h-screen items-center justify-center px-4 py-10">
      <div className="content-panel w-full max-w-md p-7">
        <h1 className="text-2xl font-semibold text-slate-900">Create account</h1>
        <p className="mt-1 text-sm text-slate-500">Set up your profile and start measuring.</p>

        <form className="mt-6 space-y-4" onSubmit={handleSubmit}>
          <label className="block text-sm">
            <span className="mb-1 block text-slate-600">Name</span>
            <input
              type="text"
              required
              value={name}
              onChange={(event) => setName(event.target.value)}
              className="w-full rounded-xl border border-slate-200 px-3 py-2 outline-none transition focus:border-brand-500"
              placeholder="Your full name"
            />
          </label>

          <label className="block text-sm">
            <span className="mb-1 block text-slate-600">Email</span>
            <input
              type="email"
              required
              value={email}
              onChange={(event) => setEmail(event.target.value)}
              className="w-full rounded-xl border border-slate-200 px-3 py-2 outline-none transition focus:border-brand-500"
              placeholder="you@example.com"
            />
          </label>

          <label className="block text-sm">
            <span className="mb-1 block text-slate-600">Password</span>
            <input
              type="password"
              required
              minLength={8}
              value={password}
              onChange={(event) => setPassword(event.target.value)}
              className="w-full rounded-xl border border-slate-200 px-3 py-2 outline-none transition focus:border-brand-500"
              placeholder="At least 8 characters"
            />
          </label>

          <label className="block text-sm">
            <span className="mb-1 block text-slate-600">Picture URL (optional)</span>
            <input
              type="url"
              value={picture}
              onChange={(event) => setPicture(event.target.value)}
              className="w-full rounded-xl border border-slate-200 px-3 py-2 outline-none transition focus:border-brand-500"
              placeholder="https://example.com/avatar.png"
            />
          </label>

          {error ? <p className="rounded-lg bg-rose-50 px-3 py-2 text-sm text-rose-600">{error}</p> : null}

          <button
            type="submit"
            disabled={submitting}
            className="w-full rounded-xl bg-slate-900 px-4 py-2 font-medium text-white transition hover:bg-slate-800 disabled:opacity-60"
          >
            {submitting ? 'Creating account...' : 'Create account'}
          </button>
        </form>

        <button
          type="button"
          onClick={startGoogleLogin}
          className="mt-3 w-full rounded-xl border border-slate-200 px-4 py-2 font-medium text-slate-700 transition hover:border-brand-200 hover:text-brand-700"
        >
          Continue with Google
        </button>

        <p className="mt-4 text-center text-sm text-slate-500">
          Already have an account?{' '}
          <Link to="/login" className="font-medium text-brand-700">
            Login
          </Link>
        </p>
      </div>
    </div>
  )
}
