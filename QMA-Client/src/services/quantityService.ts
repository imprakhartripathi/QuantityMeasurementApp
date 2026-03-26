import { apiClient } from './apiClient'
import type { QuantityInput, QuantityMeasurementHistory } from '../types'
import type { OperationType } from '../constants/units'

function buildPayload(thisQuantity: QuantityInput, thatQuantity: QuantityInput) {
  return {
    thisQuantityDTO: {
      value: thisQuantity.value,
      unit: thisQuantity.unit,
      measurementType: thisQuantity.measurementType,
    },
    thatQuantityDTO: {
      value: thatQuantity.value,
      unit: thatQuantity.unit,
      measurementType: thatQuantity.measurementType,
    },
  }
}

export const quantityService = {
  performOperation: (
    operation: OperationType,
    thisQuantity: QuantityInput,
    thatQuantity: QuantityInput,
  ) => {
    const operationPathMap: Record<OperationType, string> = {
      CONVERT: '/api/v1/quantities/convert',
      COMPARE: '/api/v1/quantities/compare',
      ADD: '/api/v1/quantities/add',
      SUBTRACT: '/api/v1/quantities/subtract',
      DIVIDE: '/api/v1/quantities/divide',
    }
    return apiClient.post<QuantityMeasurementHistory>(
      operationPathMap[operation],
      buildPayload(thisQuantity, thatQuantity),
    )
  },
}
