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
    <section className="content-panel panel-pad page-panel page-panel--wide history-page">
      <h2 className="app-page-title">History</h2>
      <p className="app-page-subtitle">Your previous quantity operations.</p>

      {loading ? <p className="mt-4 text-sm text-slate-500">Loading history...</p> : null}
      {error ? <p className="state-message state-message--error">{error}</p> : null}

      {!loading && !error && history.length === 0 ? (
        <p className="mt-4 text-sm text-slate-500">No history found yet.</p>
      ) : null}

      <div className="history-list">
        {history.map((item) => (
          <article key={item.id} className="history-item">
            <div className="history-head">
              <p className="history-op">{item.operation}</p>
              <p className="history-time">
                {item.createdAt ? new Date(item.createdAt).toLocaleString() : 'N/A'}
              </p>
            </div>
            <p className="history-line">
              Input: {item.thisValue} {item.thisUnit} ({item.thisMeasurementType}) and {item.thatValue}{' '}
              {item.thatUnit} ({item.thatMeasurementType})
            </p>
            <p className="history-line">
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
