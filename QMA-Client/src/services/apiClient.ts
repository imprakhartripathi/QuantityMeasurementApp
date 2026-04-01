import { API_BASE_URL } from '../env'

const baseUrl = API_BASE_URL.replace(/\/+$/, '')

type RequestOptions = RequestInit & {
  auth?: boolean
}

async function request<T>(path: string, options: RequestOptions = {}): Promise<T> {
  const { auth = true, headers, ...rest } = options
  const response = await fetch(`${baseUrl}${path}`, {
    ...rest,
    credentials: auth ? 'include' : 'same-origin',
    headers: {
      'Content-Type': 'application/json',
      ...headers,
    },
  })

  if (!response.ok) {
    const raw = await response.text().catch(() => '')
    const maybeJson = raw
      ? (() => {
          try {
            return JSON.parse(raw)
          } catch {
            return { message: raw }
          }
        })()
      : { message: response.statusText }
    const message = maybeJson?.message ?? 'Request failed'
    throw new Error(message)
  }

  if (response.status === 204 || response.status === 205) {
    return undefined as T
  }

  const contentType = response.headers.get('content-type') ?? ''
  if (!contentType.includes('application/json')) {
    return undefined as T
  }

  const raw = await response.text()
  if (!raw) {
    return undefined as T
  }

  return JSON.parse(raw) as T
}

export const apiClient = {
  get: <T>(path: string, options?: RequestOptions) =>
    request<T>(path, { ...options, method: 'GET' }),
  post: <T>(path: string, body?: unknown, options?: RequestOptions) =>
    request<T>(path, { ...options, method: 'POST', body: body ? JSON.stringify(body) : undefined }),
  patch: <T>(path: string, body: unknown, options?: RequestOptions) =>
    request<T>(path, { ...options, method: 'PATCH', body: JSON.stringify(body) }),
}

export { baseUrl as API_BASE_URL }
