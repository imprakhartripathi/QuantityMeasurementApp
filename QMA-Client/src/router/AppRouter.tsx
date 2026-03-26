import { Navigate, Route, Routes } from 'react-router-dom'
import { ProtectedLayout } from '../components/layout/ProtectedLayout'
import { LoginPage } from '../pages/LoginPage'
import { SignupPage } from '../pages/SignupPage'
import { DashboardPage } from '../pages/DashboardPage'
import { ProfilePage } from '../pages/ProfilePage'
import { HistoryPage } from '../pages/HistoryPage'
import { useAuth } from '../context/AuthContext'

function HomeRedirect() {
  const { isAuthenticated, loading } = useAuth()
  if (loading) {
    return <div className="flex min-h-screen items-center justify-center text-slate-500">Loading...</div>
  }
  return <Navigate to={isAuthenticated ? '/dashboard' : '/login'} replace />
}

export function AppRouter() {
  return (
    <Routes>
      <Route path="/" element={<HomeRedirect />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/signup" element={<SignupPage />} />

      <Route element={<ProtectedLayout />}>
        <Route path="/dashboard" element={<DashboardPage />} />
        <Route path="/profile" element={<ProfilePage />} />
        <Route path="/history" element={<HistoryPage />} />
      </Route>

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}
