import { useEffect, useState } from 'react'
import type { FormEvent } from 'react'
import './App.css'

type QuantityMeasurementHistory = {
  id: number
  thisValue: number
  thisUnit: string
  thisMeasurementType: string
  thatValue: number
  thatUnit: string
  thatMeasurementType: string
  operation: string
  resultString: string | null
  resultValue: number | null
  resultUnit: string | null
  resultMeasurementType: string | null
  errorMessage: string | null
  error: boolean
}

type UserProfile = {
  id: number
  name: string
  email: string
  picture: string | null
  provider: string
  history: QuantityMeasurementHistory[]
}

type AuthResponse = {
  tokenType: string
  accessToken: string
  issuedAtEpochSeconds: number
  expiresAtEpochSeconds: number
  user: UserProfile
}

type TokenTimes = {
  iat: number | null
  exp: number | null
}

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080'
const TOKEN_KEY = 'qma_access_token'

function decodeTokenTimes(token: string): TokenTimes {
  try {
    const payloadPart = token.split('.')[1]
    if (!payloadPart) {
      return { iat: null, exp: null }
    }
    const base64 = payloadPart.replace(/-/g, '+').replace(/_/g, '/')
    const padded = base64 + '='.repeat((4 - (base64.length % 4)) % 4)
    const payloadJson = atob(padded)
    const payload = JSON.parse(payloadJson) as { iat?: number; exp?: number }
    return { iat: payload.iat ?? null, exp: payload.exp ?? null }
  } catch {
    return { iat: null, exp: null }
  }
}

function formatEpoch(epochSeconds: number | null): string {
  if (!epochSeconds) {
    return 'N/A'
  }
  return new Date(epochSeconds * 1000).toLocaleString()
}

function App() {
  const [mode, setMode] = useState<'login' | 'signup'>('login')
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [picture, setPicture] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [token, setToken] = useState<string | null>(localStorage.getItem(TOKEN_KEY))
  const [profile, setProfile] = useState<UserProfile | null>(null)
  const [tokenTimes, setTokenTimes] = useState<TokenTimes>({ iat: null, exp: null })

  useEffect(() => {
    const url = new URL(window.location.href)
    const oauthToken = url.searchParams.get('token')
    if (!oauthToken) {
      return
    }
    localStorage.setItem(TOKEN_KEY, oauthToken)
    setToken(oauthToken)
    setTokenTimes(decodeTokenTimes(oauthToken))
    url.searchParams.delete('token')
    window.history.replaceState({}, document.title, url.toString())
  }, [])

  useEffect(() => {
    if (!token) {
      setProfile(null)
      setTokenTimes({ iat: null, exp: null })
      return
    }
    setTokenTimes(decodeTokenTimes(token))
    void fetchProfile(token)
  }, [token])

  async function fetchProfile(authToken: string) {
    try {
      setLoading(true)
      setError('')
      const response = await fetch(`${API_BASE_URL}/api/v1/users/me`, {
        headers: {
          Authorization: `Bearer ${authToken}`,
        },
      })
      if (!response.ok) {
        if (response.status === 401 || response.status === 403) {
          handleLogout()
        }
        const errorText = await response.text()
        throw new Error(errorText || 'Unable to fetch profile')
      }
      const data = (await response.json()) as UserProfile
      setProfile(data)
    } catch (fetchError) {
      setError(fetchError instanceof Error ? fetchError.message : 'Unable to fetch profile')
    } finally {
      setLoading(false)
    }
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    setError('')
    setLoading(true)
    try {
      const endpoint = mode === 'login' ? '/api/v1/auth/login' : '/api/v1/auth/signup'
      const payload =
        mode === 'login'
          ? { email, password }
          : { name, email, password, picture: picture || null }

      const response = await fetch(`${API_BASE_URL}${endpoint}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload),
      })

      if (!response.ok) {
        const failureBody = await response.text()
        throw new Error(failureBody || 'Authentication failed')
      }

      const data = (await response.json()) as AuthResponse
      localStorage.setItem(TOKEN_KEY, data.accessToken)
      setToken(data.accessToken)
      setProfile(data.user)
      setTokenTimes({
        iat: data.issuedAtEpochSeconds ?? null,
        exp: data.expiresAtEpochSeconds ?? null,
      })
    } catch (submitError) {
      setError(submitError instanceof Error ? submitError.message : 'Authentication failed')
    } finally {
      setLoading(false)
    }
  }

  function startGoogleLogin() {
    window.location.href = `${API_BASE_URL}/oauth2/authorization/google`
  }

  function handleLogout() {
    localStorage.removeItem(TOKEN_KEY)
    setToken(null)
    setProfile(null)
    setName('')
    setEmail('')
    setPassword('')
    setPicture('')
    setError('')
  }

  return (
    <main className="app-shell">
      <header className="app-header">
        <h1>Quantity Measurement Account</h1>
      </header>

      {!token && (
        <section className="auth-card">
          <div className="auth-mode-switch">
            <button
              type="button"
              className={mode === 'login' ? 'active' : ''}
              onClick={() => setMode('login')}
            >
              Login
            </button>
            <button
              type="button"
              className={mode === 'signup' ? 'active' : ''}
              onClick={() => setMode('signup')}
            >
              Sign Up
            </button>
          </div>

          <form onSubmit={handleSubmit} className="auth-form">
            {mode === 'signup' && (
              <>
                <label>
                  Name
                  <input
                    required
                    value={name}
                    onChange={(event) => setName(event.target.value)}
                    placeholder="Your name"
                  />
                </label>
                <label>
                  Picture URL (optional)
                  <input
                    value={picture}
                    onChange={(event) => setPicture(event.target.value)}
                    placeholder="https://..."
                  />
                </label>
              </>
            )}

            <label>
              Email
              <input
                required
                type="email"
                value={email}
                onChange={(event) => setEmail(event.target.value)}
                placeholder="you@example.com"
              />
            </label>
            <label>
              Password
              <input
                required
                type="password"
                value={password}
                onChange={(event) => setPassword(event.target.value)}
                placeholder="********"
              />
            </label>

            <button type="submit" disabled={loading}>
              {loading ? 'Please wait...' : mode === 'login' ? 'Login' : 'Create Account'}
            </button>
          </form>

          <div className="divider">OR</div>
          <button type="button" className="google-btn" onClick={startGoogleLogin}>
            Continue with Google
          </button>
          {error && <p className="error-text">{error}</p>}
        </section>
      )}

      {token && (
        <section className="profile-card">
          <div className="profile-top">
            <h2>Logged In</h2>
            <button type="button" onClick={handleLogout}>
              Logout
            </button>
          </div>

          {loading && <p>Loading profile...</p>}
          {error && <p className="error-text">{error}</p>}

          {profile && (
            <>
              <div className="profile-grid">
                <div>
                  <strong>Name:</strong> {profile.name}
                </div>
                <div>
                  <strong>Email:</strong> {profile.email}
                </div>
                <div>
                  <strong>Provider:</strong> {profile.provider}
                </div>
                <div>
                  <strong>User ID:</strong> {profile.id}
                </div>
                <div>
                  <strong>Token Issued At:</strong> {formatEpoch(tokenTimes.iat)}
                </div>
                <div>
                  <strong>Token Expires At:</strong> {formatEpoch(tokenTimes.exp)}
                </div>
              </div>

              {profile.picture && (
                <div className="avatar-block">
                  <img src={profile.picture} alt={`${profile.name} avatar`} />
                </div>
              )}

              <h3>Quantity Measurement History ({profile.history.length})</h3>
              <div className="history-container">
                {profile.history.length === 0 && <p>No history yet.</p>}
                {profile.history.map((item) => (
                  <article key={item.id} className="history-item">
                    <div>
                      <strong>Operation:</strong> {item.operation}
                    </div>
                    <div>
                      <strong>Left:</strong> {item.thisValue} {item.thisUnit} ({item.thisMeasurementType})
                    </div>
                    <div>
                      <strong>Right:</strong> {item.thatValue} {item.thatUnit} ({item.thatMeasurementType})
                    </div>
                    <div>
                      <strong>Result:</strong>{' '}
                      {item.error
                        ? `Error: ${item.errorMessage}`
                        : item.resultString ??
                          `${item.resultValue ?? ''} ${item.resultUnit ?? ''} ${item.resultMeasurementType ?? ''}`}
                    </div>
                  </article>
                ))}
              </div>
            </>
          )}
        </section>
      )}
    </main>
  )
}

export default App
