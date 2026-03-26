import { useEffect, useState } from 'react'
import { useAuth } from '../context/AuthContext'
import { userService } from '../services/userService'

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
  }, [user])

  if (!user) {
    return null
  }

  const onSave = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    setSaving(true)
    setError('')
    setMessage('')
    try {
      const updated = await userService.updateMyProfile({
        name,
        picture: picture || null,
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
    <section className="content-panel p-6">
      <h2 className="text-xl font-semibold text-slate-900">Profile</h2>
      <p className="mt-1 text-sm text-slate-500">Manage your account details.</p>

      <div className="mt-5 grid gap-5 lg:grid-cols-2">
        <div className="rounded-2xl border border-slate-200 p-4">
          <h3 className="text-sm font-semibold uppercase tracking-wide text-slate-500">Current Details</h3>
          <div className="mt-3 space-y-2 text-sm text-slate-700">
            <p>
              <span className="font-semibold">Name:</span> {user.name}
            </p>
            <p>
              <span className="font-semibold">Email:</span> {user.email}
            </p>
            <p>
              <span className="font-semibold">Provider:</span> {user.provider}
            </p>
            <p>
              <span className="font-semibold">History Count:</span> {user.history.length}
            </p>
          </div>
        </div>

        <form className="rounded-2xl border border-slate-200 p-4" onSubmit={onSave}>
          <h3 className="text-sm font-semibold uppercase tracking-wide text-slate-500">Edit Profile</h3>
          <label className="mt-3 block text-sm">
            <span className="mb-1 block text-slate-600">Name</span>
            <input
              value={name}
              onChange={(event) => setName(event.target.value)}
              className="w-full rounded-xl border border-slate-200 px-3 py-2 outline-none transition focus:border-brand-500"
              required
            />
          </label>

          <label className="mt-3 block text-sm">
            <span className="mb-1 block text-slate-600">Picture URL</span>
            <input
              value={picture}
              onChange={(event) => setPicture(event.target.value)}
              placeholder="https://example.com/avatar.png"
              className="w-full rounded-xl border border-slate-200 px-3 py-2 outline-none transition focus:border-brand-500"
            />
          </label>

          <button
            type="submit"
            disabled={saving}
            className="mt-4 rounded-xl bg-slate-900 px-4 py-2 text-sm font-semibold text-white transition hover:bg-slate-800 disabled:opacity-60"
          >
            {saving ? 'Saving...' : 'Save changes'}
          </button>

          {message ? <p className="mt-3 rounded-lg bg-emerald-50 p-2 text-sm text-emerald-700">{message}</p> : null}
          {error ? <p className="mt-3 rounded-lg bg-rose-50 p-2 text-sm text-rose-700">{error}</p> : null}
        </form>
      </div>
    </section>
  )
}
