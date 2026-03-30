import { History, Home, LogOut } from 'lucide-react'
import { useState } from 'react'
import { AvatarButton } from '../common/AvatarButton'
import { useAuth } from '../../context/useAuth'

type AppNavbarProps = {
  onOpenHome: () => void
  onOpenProfile: () => void
  onOpenHistory: () => void
  activePath: '/dashboard' | '/profile' | '/history'
}

export function AppNavbar({ onOpenHome, onOpenProfile, onOpenHistory, activePath }: AppNavbarProps) {
  const { user, logout, isLoggingOut } = useAuth()
  const [confirmingLogout, setConfirmingLogout] = useState(false)

  return (
    <>
      <header className="app-navbar">
        <div className="app-navbar__inner">
          <div className="app-navbar__brand">
          <h1 className="app-navbar__title">Quantity Measurement</h1>
            <p className="app-navbar__subtitle">Convert, compare and calculate across units.</p>
          </div>

          <div className="app-navbar__actions">
            <button
              type="button"
              onClick={onOpenHome}
              className={`icon-btn ${activePath === '/dashboard' ? 'icon-btn--active' : ''}`}
              title="Home"
              aria-current={activePath === '/dashboard' ? 'page' : undefined}
            >
              <Home size={18} />
            </button>
            <button
              type="button"
              onClick={onOpenHistory}
              className={`icon-btn ${activePath === '/history' ? 'icon-btn--active' : ''}`}
              title="History"
              aria-current={activePath === '/history' ? 'page' : undefined}
            >
              <History size={18} />
            </button>

            <AvatarButton
              name={user?.name ?? 'User'}
              picture={user?.picture}
              onClick={onOpenProfile}
              className={activePath === '/profile' ? 'avatar-btn--active' : ''}
            />
            <span className="app-user-name">{user?.name ?? 'User'}</span>

            <button
              type="button"
              onClick={() => setConfirmingLogout(true)}
              className="icon-btn icon-btn--danger"
              title="Logout"
              disabled={isLoggingOut}
            >
              <LogOut size={18} />
            </button>
          </div>
        </div>
      </header>

      {confirmingLogout ? (
        <div className="confirm-overlay" role="dialog" aria-modal="true" aria-label="Confirm logout">
          <div className="confirm-card">
            <h3>Sign out?</h3>
            <p>You will need to sign in again to access your dashboard.</p>
            <div className="confirm-actions">
              <button
                type="button"
                className="confirm-btn confirm-btn--ghost"
                onClick={() => setConfirmingLogout(false)}
                disabled={isLoggingOut}
              >
                Cancel
              </button>
              <button
                type="button"
                className="confirm-btn confirm-btn--danger"
                onClick={() => void logout()}
                disabled={isLoggingOut}
              >
                {isLoggingOut ? 'Signing out...' : 'Yes, sign out'}
              </button>
            </div>
          </div>
        </div>
      ) : null}
    </>
  )
}
