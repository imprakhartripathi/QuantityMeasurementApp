import { Navigate, Outlet, useNavigate } from 'react-router-dom'
import { AppNavbar } from './AppNavbar'
import { useAuth } from '../../context/AuthContext'

export function ProtectedLayout() {
  const { isAuthenticated, loading } = useAuth()
  const navigate = useNavigate()

  if (loading) {
    return <div className="flex min-h-screen items-center justify-center text-slate-500">Loading session...</div>
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />
  }

  return (
    <div className="min-h-screen pb-8">
      <AppNavbar onOpenProfile={() => navigate('/profile')} onOpenHistory={() => navigate('/history')} />
      <main className="page-shell">
        <Outlet />
      </main>
    </div>
  )
}
