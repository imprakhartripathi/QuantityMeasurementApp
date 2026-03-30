import { useEffect, useState } from 'react'
import { useAuth } from '../context/useAuth'
import { userService } from '../services/userService'
import fallbackAvatar from '../assets/user.png'

export function ProfilePage() {
  const { user, setUser, refreshSession } = useAuth()
  const [name, setName] = useState(user?.name ?? '')
  const [picture, setPicture] = useState(user?.picture ?? '')
  const [saving, setSaving] = useState(false)
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')

  useEffect(() => {
    setName(user?.name ?? '')
    setPicture(user?.picture ?? '')
    setMessage('')
    setError('')
  }, [user])

  if (!user) {
    return null
  }

  const normalizedName = name.trim()
  const normalizedPicture = picture.trim() || null
  const hasChanges = normalizedName !== user.name || normalizedPicture !== (user.picture ?? null)

  const onSave = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    if (!hasChanges) {
      return
    }
    setSaving(true)
    setError('')
    setMessage('')
    try {
      const updated = await userService.updateMyProfile({
        name: normalizedName,
        picture: normalizedPicture,
      })
      setUser(updated)
      await refreshSession()
      setMessage('Profile updated successfully.')
    } catch (saveError) {
      setError(saveError instanceof Error ? saveError.message : 'Unable to update profile')
    } finally {
      setSaving(false)
    }
  }

  return (
    <section className="content-panel panel-pad page-panel page-panel--wide profile-page">
      <h2 className="app-page-title">Profile</h2>
      <p className="app-page-subtitle">Manage your account details.</p>

      <div className="profile-grid">
        <div className="sub-card">
          <h3 className="panel-label">Current Details</h3>
          <div style={{ marginTop: '0.8rem' }}>
            <img
              src={normalizedPicture ?? user.picture ?? fallbackAvatar}
              alt={`${user.name} preview`}
              style={{
                height: '92px',
                width: '92px',
                borderRadius: '999px',
                objectFit: 'cover',
                border: '2px solid #fff',
                boxShadow: '0 0 0 2px #d9e4f0',
              }}
            />
          </div>
          <div className="kv-list">
            <p>
              <span style={{ fontWeight: 700 }}>Name:</span> {user.name}
            </p>
            <p>
              <span style={{ fontWeight: 700 }}>Email:</span> {user.email}
            </p>
            <p>
              <span style={{ fontWeight: 700 }}>Provider:</span> {user.provider}
            </p>
            <p>
              <span style={{ fontWeight: 700 }}>History Count:</span> {user.history.length}
            </p>
          </div>
        </div>

        <form className="sub-card" onSubmit={onSave}>
          <h3 className="panel-label">Edit Profile</h3>
          <div className="form-stack">
            <label>
              <span className="panel-label" style={{ display: 'block', marginBottom: '0.45rem' }}>
                Name
              </span>
              <input
                value={name}
                onChange={(event) => setName(event.target.value)}
                className="modern-field"
                required
              />
            </label>

            <label>
              <span className="panel-label" style={{ display: 'block', marginBottom: '0.45rem' }}>
                Picture URL
              </span>
              <input
                value={picture}
                onChange={(event) => setPicture(event.target.value)}
                placeholder="https://example.com/avatar.png"
                className="modern-field"
              />
            </label>
          </div>

          <button
            type="submit"
            disabled={saving || !hasChanges}
            className="modern-btn-primary"
            style={{ marginTop: '1rem' }}
          >
            {saving ? 'Saving...' : 'Save changes'}
          </button>

          {message ? <p className="state-message state-message--ok">{message}</p> : null}
          {error ? <p className="state-message state-message--error">{error}</p> : null}
        </form>
      </div>
    </section>
  )
}
