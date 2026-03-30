import type { ReactElement } from 'react'
import { Navigate, Route, Routes } from 'react-router-dom'
import { ProtectedLayout } from '../components/layout/ProtectedLayout'
import { LoginPage } from '../pages/LoginPage'
import { SignupPage } from '../pages/SignupPage'
import { DashboardPage } from '../pages/DashboardPage'
import { ProfilePage } from '../pages/ProfilePage'
import { HistoryPage } from '../pages/HistoryPage'
import { OAuthCallbackPage } from '../pages/OAuthCallbackPage'
import { useAuth } from '../context/useAuth'

function HomeRedirect() {
  const { isAuthenticated, loading } = useAuth()
  if (loading) {
    return <div className="flex min-h-screen items-center justify-center text-slate-500">Loading...</div>
  }
  return <Navigate to={isAuthenticated ? '/dashboard' : '/login'} replace />
}

function PublicOnlyRoute({ children }: { children: ReactElement }) {
  const { isAuthenticated, loading } = useAuth()
  if (loading) {
    return <div className="app-shell app-page">Loading...</div>
  }
  if (isAuthenticated) {
    return <Navigate to="/dashboard" replace />
  }
  return children
}

export function AppRouter() {
  return (
    <Routes>
      <Route path="/" element={<HomeRedirect />} />
      <Route
        path="/login"
        element={
          <PublicOnlyRoute>
            <LoginPage />
          </PublicOnlyRoute>
        }
      />
      <Route
        path="/signup"
        element={
          <PublicOnlyRoute>
            <SignupPage />
          </PublicOnlyRoute>
        }
      />
      <Route path="/oauth/callback" element={<OAuthCallbackPage />} />

      <Route element={<ProtectedLayout />}>
        <Route path="/dashboard" element={<DashboardPage />} />
        <Route path="/profile" element={<ProfilePage />} />
        <Route path="/history" element={<HistoryPage />} />
      </Route>

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}
