import { useMemo, useState } from 'react'
import { MEASUREMENT_TYPES, OPERATION_LABELS, UNITS_BY_TYPE } from '../constants/units'
import type { MeasurementType, OperationType } from '../constants/units'
import { quantityService } from '../services/quantityService'
import type { QuantityMeasurementHistory } from '../types'
import '../styles/dashboard.scss'

export function DashboardPage() {
  const [measurementType, setMeasurementType] = useState<MeasurementType>('LENGTH')
  const [operation, setOperation] = useState<OperationType>('CONVERT')
  const [fromValue, setFromValue] = useState(1)
  const [toValue, setToValue] = useState(100)
  const [fromUnit, setFromUnit] = useState(UNITS_BY_TYPE.LENGTH[0])
  const [toUnit, setToUnit] = useState(UNITS_BY_TYPE.LENGTH[UNITS_BY_TYPE.LENGTH.length - 1])
  const [result, setResult] = useState<QuantityMeasurementHistory | null>(null)
  const [submitting, setSubmitting] = useState(false)
  const [error, setError] = useState('')

  const unitOptions = useMemo(() => UNITS_BY_TYPE[measurementType], [measurementType])

  const onTypeChange = (type: MeasurementType) => {
    setMeasurementType(type)
    const units = UNITS_BY_TYPE[type]
    setFromUnit(units[0])
    setToUnit(units[Math.min(1, units.length - 1)])
    setResult(null)
    setError('')
  }

  const runOperation = async () => {
    setSubmitting(true)
    setError('')
    try {
      const response = await quantityService.performOperation(
        operation,
        { value: fromValue, unit: fromUnit, measurementType },
        { value: toValue, unit: toUnit, measurementType },
      )
      setResult(response)
    } catch (operationError) {
      setError(operationError instanceof Error ? operationError.message : 'Operation failed')
      setResult(null)
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <section className="dashboard-grid">
      <div className="content-panel p-5">
        <h2 className="text-lg font-semibold text-slate-900">Choose Type</h2>

        <div className="mt-4 grid gap-3 md:grid-cols-4">
          {MEASUREMENT_TYPES.map((item) => (
            <button
              key={item.key}
              type="button"
              onClick={() => onTypeChange(item.key)}
              className={`type-card ${measurementType === item.key ? 'active' : ''}`}
            >
              <span className="text-3xl">{item.icon}</span>
              <span className="text-sm font-medium">{item.label}</span>
            </button>
          ))}
        </div>

        <h3 className="mt-6 text-sm font-semibold uppercase tracking-wide text-slate-500">Operation</h3>
        <div className="mt-2 flex flex-wrap gap-2">
          {(Object.keys(OPERATION_LABELS) as OperationType[]).map((item) => (
            <button
              key={item}
              type="button"
              onClick={() => setOperation(item)}
              className={`rounded-full border px-3 py-1 text-sm transition ${
                item === operation
                  ? 'border-brand-500 bg-brand-50 text-brand-700'
                  : 'border-slate-200 text-slate-600 hover:border-brand-200 hover:text-brand-700'
              }`}
            >
              {OPERATION_LABELS[item]}
            </button>
          ))}
        </div>

        <div className="mt-6 grid gap-4 md:grid-cols-2">
          <div className="rounded-2xl border border-slate-200 p-4">
            <label className="mb-1 block text-xs font-semibold uppercase tracking-wide text-slate-500">
              From
            </label>
            <input
              type="number"
              value={fromValue}
              onChange={(event) => setFromValue(Number(event.target.value))}
              className="w-full rounded-lg border border-slate-200 px-3 py-2 text-2xl font-semibold outline-none focus:border-brand-400"
            />
            <select
              value={fromUnit}
              onChange={(event) => setFromUnit(event.target.value)}
              className="mt-3 w-full rounded-lg border border-slate-200 px-3 py-2 outline-none focus:border-brand-400"
            >
              {unitOptions.map((item) => (
                <option key={item} value={item}>
                  {item}
                </option>
              ))}
            </select>
          </div>

          <div className="rounded-2xl border border-slate-200 p-4">
            <label className="mb-1 block text-xs font-semibold uppercase tracking-wide text-slate-500">
              To
            </label>
            <input
              type="number"
              value={toValue}
              onChange={(event) => setToValue(Number(event.target.value))}
              className="w-full rounded-lg border border-slate-200 px-3 py-2 text-2xl font-semibold outline-none focus:border-brand-400"
            />
            <select
              value={toUnit}
              onChange={(event) => setToUnit(event.target.value)}
              className="mt-3 w-full rounded-lg border border-slate-200 px-3 py-2 outline-none focus:border-brand-400"
            >
              {unitOptions.map((item) => (
                <option key={item} value={item}>
                  {item}
                </option>
              ))}
            </select>
          </div>
        </div>

        <div className="mt-5 flex items-center gap-3">
          <button
            type="button"
            disabled={submitting}
            onClick={() => void runOperation()}
            className="rounded-xl bg-brand-500 px-5 py-2.5 font-semibold text-white transition hover:bg-brand-700 disabled:cursor-not-allowed disabled:opacity-60"
          >
            {submitting ? 'Running...' : `Run ${OPERATION_LABELS[operation]}`}
          </button>
          <p className="text-xs text-slate-500">All backend-supported units are available in selectors.</p>
        </div>

        {error ? <p className="mt-4 rounded-lg bg-rose-50 p-3 text-sm text-rose-700">{error}</p> : null}
      </div>

      <div className="content-panel p-5">
        <h3 className="text-lg font-semibold text-slate-900">Latest Result</h3>
        {!result ? (
          <p className="mt-3 text-sm text-slate-500">Run an operation to see results here.</p>
        ) : (
          <div className="mt-4 space-y-3 text-sm">
            <div className="rounded-xl border border-slate-200 p-3">
              <span className="font-semibold text-slate-700">Operation:</span> {result.operation}
            </div>
            <div className="rounded-xl border border-slate-200 p-3">
              <span className="font-semibold text-slate-700">Input:</span> {result.thisValue} {result.thisUnit} and{' '}
              {result.thatValue} {result.thatUnit}
            </div>
            <div className="rounded-xl border border-slate-200 p-3">
              <span className="font-semibold text-slate-700">Output:</span>{' '}
              {result.error
                ? result.errorMessage
                : result.resultString ??
                  `${result.resultValue ?? ''} ${result.resultUnit ?? ''} ${result.resultMeasurementType ?? ''}`}
            </div>
            <div className="rounded-xl border border-slate-200 p-3">
              <span className="font-semibold text-slate-700">Timestamp:</span>{' '}
              {result.createdAt ? new Date(result.createdAt).toLocaleString() : 'N/A'}
            </div>
          </div>
        )}
      </div>
    </section>
  )
}
