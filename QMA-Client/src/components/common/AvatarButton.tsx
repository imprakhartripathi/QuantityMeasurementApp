import { useMemo, useState } from 'react'
import fallbackAvatar from '../../assets/user.png'

type AvatarButtonProps = {
  name: string
  picture?: string | null
  onClick?: () => void
}

export function AvatarButton({ name, picture, onClick }: AvatarButtonProps) {
  const [hasError, setHasError] = useState(false)
  const initials = useMemo(
    () =>
      name
        .split(' ')
        .filter(Boolean)
        .slice(0, 2)
        .map((value) => value[0]?.toUpperCase())
        .join(''),
    [name],
  )
  const avatarSrc = !hasError ? picture || fallbackAvatar : null

  return (
    <button
      type="button"
      onClick={onClick}
      className="h-10 w-10 overflow-hidden rounded-full border-2 border-slate-200 shadow-sm"
      title="Profile"
    >
      {avatarSrc ? (
        <img
          src={avatarSrc}
          alt={`${name} profile`}
          className="h-full w-full object-cover"
          onError={() => setHasError(true)}
        />
      ) : (
        <span className="flex h-full w-full items-center justify-center bg-brand-100 text-xs font-bold text-brand-900">
          {initials || 'U'}
        </span>
      )}
    </button>
  )
}
