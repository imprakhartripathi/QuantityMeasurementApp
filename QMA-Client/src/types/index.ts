export type QuantityMeasurementHistory = {
  id: number
  createdAt: string
  thisValue: number
  thisUnit: string
  thisMeasurementType: string
  thatValue: number
  thatUnit: string
  thatMeasurementType: string
  operation: string
  resultString: string | null
  resultValue: number | null
  resultUnit: string | null
  resultMeasurementType: string | null
  errorMessage: string | null
  error: boolean
}

export type UserProfile = {
  id: number
  name: string
  email: string
  picture: string | null
  provider: string
  history: QuantityMeasurementHistory[]
}

export type AuthResponse = {
  tokenType: string
  accessToken: string
  issuedAtEpochSeconds: number
  expiresAtEpochSeconds: number
  user: UserProfile
}

export type QuantityInput = {
  value: number
  unit: string
  measurementType: string
}
