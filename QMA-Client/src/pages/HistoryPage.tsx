import { useEffect, useState } from 'react'
import { userService } from '../services/userService'
import type { QuantityMeasurementHistory } from '../types'

export function HistoryPage() {
  const [history, setHistory] = useState<QuantityMeasurementHistory[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    const fetchHistory = async () => {
      setLoading(true)
      try {
        const data = await userService.getMyHistory()
        setHistory(data)
      } catch (historyError) {
        setError(historyError instanceof Error ? historyError.message : 'Unable to load history')
      } finally {
        setLoading(false)
      }
    }
    void fetchHistory()
  }, [])

  return (
    <section className="content-panel p-6">
      <h2 className="text-xl font-semibold text-slate-900">History</h2>
      <p className="mt-1 text-sm text-slate-500">Your previous quantity operations.</p>

      {loading ? <p className="mt-4 text-sm text-slate-500">Loading history...</p> : null}
      {error ? <p className="mt-4 rounded-lg bg-rose-50 p-3 text-sm text-rose-700">{error}</p> : null}

      {!loading && !error && history.length === 0 ? (
        <p className="mt-4 text-sm text-slate-500">No history found yet.</p>
      ) : null}

      <div className="mt-4 space-y-3">
        {history.map((item) => (
          <article key={item.id} className="rounded-xl border border-slate-200 p-4">
            <div className="flex flex-wrap items-center justify-between gap-2">
              <p className="font-semibold text-slate-700">{item.operation}</p>
              <p className="text-xs text-slate-500">
                {item.createdAt ? new Date(item.createdAt).toLocaleString() : 'N/A'}
              </p>
            </div>
            <p className="mt-2 text-sm text-slate-600">
              Input: {item.thisValue} {item.thisUnit} ({item.thisMeasurementType}) and {item.thatValue}{' '}
              {item.thatUnit} ({item.thatMeasurementType})
            </p>
            <p className="mt-1 text-sm text-slate-700">
              Result:{' '}
              {item.error
                ? item.errorMessage
                : item.resultString ??
                  `${item.resultValue ?? ''} ${item.resultUnit ?? ''} ${item.resultMeasurementType ?? ''}`}
            </p>
          </article>
        ))}
      </div>
    </section>
  )
}
