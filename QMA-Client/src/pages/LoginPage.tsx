import { useEffect, useState } from 'react'
import { Link, Navigate, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export function LoginPage() {
  const { isAuthenticated, login, startGoogleLogin } = useAuth()
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
    <div className="grid min-h-screen lg:grid-cols-2">
      <aside className="hidden bg-slate-900 p-12 text-white lg:flex lg:flex-col lg:justify-between">
        <div>
          <p className="text-sm uppercase tracking-[0.2em] text-brand-200">Quantity Measurement</p>
          <h1 className="mt-4 text-4xl font-semibold leading-tight">
            Work with units in one secure, modern workspace.
          </h1>
        </div>
        <p className="max-w-md text-sm text-slate-300">
          Compare, convert and calculate across length, weight, volume and temperature with persistent user history.
        </p>
      </aside>

      <main className="flex items-center justify-center px-4 py-10">
        <div className="content-panel w-full max-w-md p-7">
          <h2 className="text-2xl font-semibold text-slate-900">Welcome back</h2>
          <p className="mt-1 text-sm text-slate-500">Login to continue to your dashboard.</p>

          <form className="mt-6 space-y-4" onSubmit={handleSubmit}>
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
                value={password}
                onChange={(event) => setPassword(event.target.value)}
                className="w-full rounded-xl border border-slate-200 px-3 py-2 outline-none transition focus:border-brand-500"
                placeholder="••••••••"
              />
            </label>

            {error ? <p className="rounded-lg bg-rose-50 px-3 py-2 text-sm text-rose-600">{error}</p> : null}

            <button
              type="submit"
              disabled={submitting}
              className="w-full rounded-xl bg-slate-900 px-4 py-2 font-medium text-white transition hover:bg-slate-800 disabled:opacity-60"
            >
              {submitting ? 'Signing in...' : 'Login'}
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
            New here?{' '}
            <Link to="/signup" className="font-medium text-brand-700">
              Create account
            </Link>
          </p>
        </div>
      </main>
    </div>
  )
}
