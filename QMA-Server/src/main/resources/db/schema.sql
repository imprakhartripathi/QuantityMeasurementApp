CREATE TABLE IF NOT EXISTS quantity_measurements (
    id VARCHAR(64) PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    operation_type VARCHAR(32) NOT NULL,
    left_value DOUBLE PRECISION NOT NULL,
    left_unit_name VARCHAR(32) NOT NULL,
    left_measurement_type VARCHAR(32) NOT NULL,
    right_value DOUBLE PRECISION,
    right_unit_name VARCHAR(32),
    right_measurement_type VARCHAR(32),
    result_value DOUBLE PRECISION,
    result_unit_name VARCHAR(32),
    result_measurement_type VARCHAR(32),
    comparison_result BOOLEAN,
    scalar_result DOUBLE PRECISION,
    error BOOLEAN NOT NULL,
    error_message VARCHAR(255)
);

CREATE INDEX IF NOT EXISTS idx_quantity_measurements_operation
    ON quantity_measurements (operation_type);

CREATE INDEX IF NOT EXISTS idx_quantity_measurements_type
    ON quantity_measurements (left_measurement_type);

CREATE TABLE IF NOT EXISTS quantity_measurement_history (
    history_id BIGSERIAL PRIMARY KEY,
    measurement_id VARCHAR(64) NOT NULL,
    operation_type VARCHAR(32) NOT NULL,
    recorded_at TIMESTAMP NOT NULL
);
