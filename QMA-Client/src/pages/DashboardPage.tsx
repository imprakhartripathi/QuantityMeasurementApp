import { useEffect, useMemo, useState } from 'react'
import { MEASUREMENT_TYPES, OPERATION_LABELS, UNITS_BY_TYPE } from '../constants/units'
import type { MeasurementType, OperationType } from '../constants/units'
import { quantityService } from '../services/quantityService'
import type { QuantityInput, QuantityMeasurementHistory } from '../types'

const ARITHMETIC_OPERATIONS: OperationType[] = ['ADD', 'SUBTRACT', 'DIVIDE']

type ComparisonOutcome = {
  label: string
  detail?: string
}

export function DashboardPage() {
  const [measurementType, setMeasurementType] = useState<MeasurementType>('LENGTH')
  const [operation, setOperation] = useState<OperationType>('CONVERT')
  const [convertValue, setConvertValue] = useState(1)
  const [convertFromUnit, setConvertFromUnit] = useState(UNITS_BY_TYPE.LENGTH[0])
  const [convertToUnit, setConvertToUnit] = useState(UNITS_BY_TYPE.LENGTH[1])
  const [leftValue, setLeftValue] = useState(1)
  const [leftUnit, setLeftUnit] = useState(UNITS_BY_TYPE.LENGTH[0])
  const [rightValue, setRightValue] = useState(100)
  const [rightUnit, setRightUnit] = useState(UNITS_BY_TYPE.LENGTH[1])
  const [result, setResult] = useState<QuantityMeasurementHistory | null>(null)
  const [comparisonOutcome, setComparisonOutcome] = useState<ComparisonOutcome | null>(null)
  const [submitting, setSubmitting] = useState(false)
  const [error, setError] = useState('')

  const unitOptions = useMemo(() => UNITS_BY_TYPE[measurementType], [measurementType])
  const operations = useMemo<OperationType[]>(
    () => (measurementType === 'TEMPERATURE' ? ['CONVERT', 'COMPARE'] : ['CONVERT', 'COMPARE', ...ARITHMETIC_OPERATIONS]),
    [measurementType],
  )

  useEffect(() => {
    const [firstUnit, secondUnit] = unitOptions
    setConvertFromUnit(firstUnit)
    setConvertToUnit(secondUnit ?? firstUnit)
    setLeftUnit(firstUnit)
    setRightUnit(secondUnit ?? firstUnit)
    if (!operations.includes(operation)) {
      setOperation(operations[0])
    }
    setResult(null)
    setComparisonOutcome(null)
    setError('')
  }, [measurementType, operation, operations, unitOptions])

  const convertQuantity = (quantity: QuantityInput, targetUnit: string) =>
    quantityService.performOperation('CONVERT', quantity, {
      value: 0,
      unit: targetUnit,
      measurementType: quantity.measurementType,
    })

  const runConvert = async () => {
    const response = await convertQuantity(
      { value: convertValue, unit: convertFromUnit, measurementType },
      convertToUnit,
    )
    setResult(response)
    setComparisonOutcome(null)
  }

  const runCompare = async () => {
    const lhs: QuantityInput = { value: leftValue, unit: leftUnit, measurementType }
    const rhs: QuantityInput = { value: rightValue, unit: rightUnit, measurementType }
    const response = await quantityService.performOperation('COMPARE', lhs, rhs)
    setResult(response)

    if (String(response.resultString).toLowerCase() === 'true') {
      setComparisonOutcome({ label: 'LHS is equal to RHS' })
      return
    }

    if (leftUnit === rightUnit) {
      const label = leftValue > rightValue ? 'LHS is greater than RHS' : 'RHS is greater than LHS'
      setComparisonOutcome({ label })
      return
    }

    const lhsInRhsUnit = await convertQuantity(lhs, rightUnit)
    const converted = lhsInRhsUnit.resultValue ?? 0
    const label = converted > rightValue ? 'LHS is greater than RHS' : 'RHS is greater than LHS'
    setComparisonOutcome({
      label,
      detail: `LHS converted to ${rightUnit}: ${converted.toFixed(4)} ${rightUnit}`,
    })
  }

  const runArithmetic = async () => {
    const response = await quantityService.performOperation(
      operation,
      { value: leftValue, unit: leftUnit, measurementType },
      { value: rightValue, unit: rightUnit, measurementType },
    )
    setResult(response)
    setComparisonOutcome(null)
  }

  const runOperation = async () => {
    setSubmitting(true)
    setError('')
    try {
      if (operation === 'CONVERT') {
        await runConvert()
      } else if (operation === 'COMPARE') {
        await runCompare()
      } else {
        await runArithmetic()
      }
    } catch (operationError) {
      setError(operationError instanceof Error ? operationError.message : 'Operation failed')
      setResult(null)
      setComparisonOutcome(null)
    } finally {
      setSubmitting(false)
    }
  }

  const formatInputSummary = () => {
    if (!result) {
      return ''
    }
    if (result.operation === 'CONVERT') {
      return `${result.thisValue} ${result.thisUnit} -> ${result.thatUnit ?? convertToUnit}`
    }
    return `LHS: ${result.thisValue} ${result.thisUnit} | RHS: ${result.thatValue} ${result.thatUnit}`
  }

  const formatOutputSummary = () => {
    if (!result) {
      return ''
    }
    if (result.error) {
      return result.errorMessage ?? 'Operation failed'
    }
    if (result.resultString) {
      return result.resultString
    }
    const value = result.resultValue ?? ''
    const unit = result.resultUnit ? ` ${result.resultUnit}` : ''
    const measurementType = result.resultMeasurementType ? ` (${result.resultMeasurementType})` : ''
    return `${value}${unit}${measurementType}`.trim()
  }

  const renderOperationForm = () => {
    if (operation === 'CONVERT') {
      return (
        <div className="dash-inputs dash-inputs--convert">
          <div className="unit-card">
            <label className="panel-label">Value</label>
            <input
              type="number"
              className="modern-field"
              value={convertValue}
              onChange={(event) => setConvertValue(Number(event.target.value))}
            />
          </div>
          <div className="unit-card">
            <label className="panel-label">From Unit</label>
            <select
              className="modern-field"
              value={convertFromUnit}
              onChange={(event) => setConvertFromUnit(event.target.value)}
            >
              {unitOptions.map((item) => (
                <option key={item} value={item}>
                  {item}
                </option>
              ))}
            </select>
          </div>
          <div className="unit-card">
            <label className="panel-label">To Unit</label>
            <select className="modern-field" value={convertToUnit} onChange={(event) => setConvertToUnit(event.target.value)}>
              {unitOptions.map((item) => (
                <option key={item} value={item}>
                  {item}
                </option>
              ))}
            </select>
          </div>
        </div>
      )
    }

    return (
      <div className="dash-inputs dash-inputs--paired">
        <div className="unit-card">
          <label className="panel-label">{operation === 'COMPARE' ? 'LHS' : 'Left Value'}</label>
          <input
            type="number"
            className="modern-field"
            value={leftValue}
            onChange={(event) => setLeftValue(Number(event.target.value))}
          />
          <select className="modern-field" value={leftUnit} onChange={(event) => setLeftUnit(event.target.value)}>
            {unitOptions.map((item) => (
              <option key={item} value={item}>
                {item}
              </option>
            ))}
          </select>
        </div>
        <div className="unit-card">
          <label className="panel-label">{operation === 'COMPARE' ? 'RHS' : 'Right Value'}</label>
          <input
            type="number"
            className="modern-field"
            value={rightValue}
            onChange={(event) => setRightValue(Number(event.target.value))}
          />
          <select className="modern-field" value={rightUnit} onChange={(event) => setRightUnit(event.target.value)}>
            {unitOptions.map((item) => (
              <option key={item} value={item}>
                {item}
              </option>
            ))}
          </select>
        </div>
      </div>
    )
  }

  return (
    <section className="dash-layout dash-layout--viewport dash-layout--balanced">
      <div className="content-panel panel-pad">
        <h2 className="app-page-title">Quantity Measurement</h2>
        <p className="app-page-subtitle">Choose type and run one operation at a time with a focused form.</p>

        <div className="dash-types dash-types--compact">
          {MEASUREMENT_TYPES.map((item) => (
            <button
              key={item.key}
              type="button"
              onClick={() => setMeasurementType(item.key)}
              className={`type-card ${measurementType === item.key ? 'active' : ''}`}
            >
              <span style={{ fontSize: '1.8rem' }}>{item.icon}</span>
              <span>{item.label}</span>
            </button>
          ))}
        </div>

        <h3 className="panel-label" style={{ marginTop: '1.2rem' }}>
          Operation
        </h3>
        <div className="op-chips op-chips--balanced">
          {operations.map((item) => (
            <button
              key={item}
              type="button"
              onClick={() => setOperation(item)}
              className={`operation-chip ${item === operation ? 'active' : ''}`}
            >
              {OPERATION_LABELS[item]}
            </button>
          ))}
        </div>

        {renderOperationForm()}

        <div className="dash-run-row">
          <button type="button" disabled={submitting} onClick={() => void runOperation()} className="modern-btn-primary">
            {submitting ? 'Running...' : `Run ${OPERATION_LABELS[operation]}`}
          </button>
          <p className="muted-note">
            {measurementType === 'TEMPERATURE'
              ? 'Temperature currently supports only Convert and Compare.'
              : 'All backend-supported units are available in selectors.'}
          </p>
        </div>

        {error ? <p className="state-message state-message--error">{error}</p> : null}
      </div>

      <div className="content-panel panel-pad">
        <h3 className="app-page-title">Latest Result</h3>
        <p className="app-page-subtitle">Live output from your most recent operation.</p>
        {!result ? (
          <div className="info-empty">
            <p className="app-page-subtitle">Run an operation to see results here.</p>
          </div>
        ) : (
          <div className="info-grid">
            <div className="info-row">
              <span style={{ fontWeight: 700 }}>Operation:</span> {result.operation}
            </div>
            <div className="info-row">
              <span style={{ fontWeight: 700 }}>Input:</span> {formatInputSummary()}
            </div>
            <div className="info-row">
              <span style={{ fontWeight: 700 }}>Output:</span> {formatOutputSummary()}
            </div>
            {comparisonOutcome ? (
              <div className="info-row">
                <span style={{ fontWeight: 700 }}>Comparison:</span> {comparisonOutcome.label}
                {comparisonOutcome.detail ? <div className="muted-note">{comparisonOutcome.detail}</div> : null}
              </div>
            ) : null}
            <div className="info-row">
              <span style={{ fontWeight: 700 }}>Timestamp:</span>{' '}
              {result.createdAt ? new Date(result.createdAt).toLocaleString() : 'N/A'}
            </div>
          </div>
        )}
      </div>
    </section>
  )
}
