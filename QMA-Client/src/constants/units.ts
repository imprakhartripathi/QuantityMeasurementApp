export type MeasurementType = 'LENGTH' | 'WEIGHT' | 'VOLUME' | 'TEMPERATURE'
export type OperationType = 'CONVERT' | 'COMPARE' | 'ADD' | 'SUBTRACT' | 'DIVIDE'

export const MEASUREMENT_TYPES: Array<{ key: MeasurementType; label: string; icon: string }> = [
  { key: 'LENGTH', label: 'Length', icon: '📏' },
  { key: 'TEMPERATURE', label: 'Temperature', icon: '🌡️' },
  { key: 'VOLUME', label: 'Volume', icon: '🧪' },
  { key: 'WEIGHT', label: 'Weight', icon: '⚖️' },
]

export const UNITS_BY_TYPE: Record<MeasurementType, string[]> = {
  LENGTH: ['FEET', 'INCH', 'YARDS', 'CENTIMETERS', 'METERS'],
  WEIGHT: ['KILOGRAM', 'GRAM', 'POUND', 'OUNCE'],
  VOLUME: ['LITRE', 'MILLILITRE', 'GALLON'],
  TEMPERATURE: ['CELSIUS', 'FAHRENHEIT', 'KELVIN'],
}

export const OPERATION_LABELS: Record<OperationType, string> = {
  CONVERT: 'Convert',
  COMPARE: 'Compare',
  ADD: 'Add',
  SUBTRACT: 'Subtract',
  DIVIDE: 'Divide',
}
