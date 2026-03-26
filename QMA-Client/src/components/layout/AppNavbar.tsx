import { History, LogOut } from 'lucide-react'
import { AvatarButton } from '../common/AvatarButton'
import { useAuth } from '../../context/AuthContext'

type AppNavbarProps = {
  onOpenProfile: () => void
  onOpenHistory: () => void
}

export function AppNavbar({ onOpenProfile, onOpenHistory }: AppNavbarProps) {
  const { user, logout } = useAuth()

  return (
    <header className="sticky top-3 z-20 mb-4 rounded-2xl border border-slate-200 bg-white/90 px-4 py-3 shadow-card backdrop-blur">
      <div className="page-shell flex items-center justify-between">
        <div>
          <h1 className="text-lg font-semibold text-slate-900">Quantity Measurement</h1>
          <p className="text-xs text-slate-500">Convert, compare and calculate across units.</p>
        </div>

        <div className="flex items-center gap-2">
          <button
            type="button"
            onClick={onOpenHistory}
            className="inline-flex items-center justify-center rounded-full border border-slate-200 p-2 text-slate-600 transition hover:border-brand-200 hover:text-brand-700"
            title="History"
          >
            <History size={18} />
          </button>

          <AvatarButton name={user?.name ?? 'User'} picture={user?.picture} onClick={onOpenProfile} />

          <button
            type="button"
            onClick={() => void logout()}
            className="inline-flex items-center justify-center rounded-full border border-slate-200 p-2 text-slate-600 transition hover:border-rose-200 hover:text-rose-600"
            title="Logout"
          >
            <LogOut size={18} />
          </button>
        </div>
      </div>
    </header>
  )
}
