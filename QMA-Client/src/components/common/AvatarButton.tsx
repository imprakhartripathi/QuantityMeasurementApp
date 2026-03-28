import { useMemo, useState } from 'react'
import fallbackAvatar from '../../assets/user.png'

type AvatarButtonProps = {
  name: string
  picture?: string | null
  onClick?: () => void
  className?: string
}

export function AvatarButton({ name, picture, onClick, className = '' }: AvatarButtonProps) {
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
    <button type="button" onClick={onClick} className={`avatar-btn ${className}`.trim()} title="Profile">
      {avatarSrc ? (
        <img
          src={avatarSrc}
          alt={`${name} profile`}
          onError={() => setHasError(true)}
        />
      ) : (
        <span className="avatar-fallback">{initials || 'U'}</span>
      )}
    </button>
  )
}
