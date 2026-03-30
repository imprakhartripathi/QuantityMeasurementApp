import { Navigate, Outlet, useLocation, useNavigate } from 'react-router-dom'
import { AppNavbar } from './AppNavbar'
import { AppFooter } from './AppFooter'
import { useAuth } from '../../context/useAuth'

export function ProtectedLayout() {
  const { isAuthenticated, loading, isLoggingOut } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()

  const currentPath = location.pathname.startsWith('/profile')
    ? '/profile'
    : location.pathname.startsWith('/history')
      ? '/history'
      : '/dashboard'

  if (loading) {
    return <div className="app-shell app-page">Loading session...</div>
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />
  }

  return (
    <div className="app-shell">
      <AppNavbar
        onOpenHome={() => navigate('/dashboard')}
        onOpenProfile={() => navigate('/profile')}
        onOpenHistory={() => navigate('/history')}
        activePath={currentPath}
      />
      <main className={`app-page ${isLoggingOut ? 'app-page--locked' : ''}`}>
        <Outlet />
      </main>
      <AppFooter />
      {isLoggingOut ? (
        <div className="app-blocking-loader" role="status" aria-live="polite" aria-label="Logging out">
          <div className="app-blocking-loader__card">
            <span className="app-spinner" aria-hidden="true" />
            <p>Signing you out...</p>
          </div>
        </div>
      ) : null}
    </div>
  )
}
