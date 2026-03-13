package com.imprakhartripathi.qmaserver.quantitymeasurement.repository;

import com.imprakhartripathi.qmaserver.quantitymeasurement.exception.DatabaseException;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityDTO;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityMeasurementEntity;
import com.imprakhartripathi.qmaserver.quantitymeasurement.util.ApplicationConfig;
import com.imprakhartripathi.qmaserver.quantitymeasurement.util.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class QuantityMeasurementDatabaseRepository implements IQuantityMeasurementRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuantityMeasurementDatabaseRepository.class);

    private static final String INSERT_MEASUREMENT = """
            INSERT INTO quantity_measurements (
                id, created_at, operation_type,
                left_value, left_unit_name, left_measurement_type,
                right_value, right_unit_name, right_measurement_type,
                result_value, result_unit_name, result_measurement_type,
                comparison_result, scalar_result, error, error_message
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String INSERT_HISTORY = """
            INSERT INTO quantity_measurement_history (measurement_id, operation_type, recorded_at)
            VALUES (?, ?, ?)
            """;

    private final ConnectionPool connectionPool;

    public QuantityMeasurementDatabaseRepository(ApplicationConfig config) {
        this.connectionPool = new ConnectionPool(config);
        initializeSchema(config.getSchemaLocation());
    }

    @Override
    public QuantityMeasurementEntity save(QuantityMeasurementEntity entity) {
        Objects.requireNonNull(entity, "Entity must not be null");
        Connection connection = connectionPool.acquireConnection();
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement measurementStatement = connection.prepareStatement(INSERT_MEASUREMENT);
                 PreparedStatement historyStatement = connection.prepareStatement(INSERT_HISTORY)) {
                bindMeasurement(measurementStatement, entity);
                measurementStatement.executeUpdate();

                historyStatement.setString(1, entity.getId());
                historyStatement.setString(2, entity.getOperationType());
                historyStatement.setTimestamp(3, Timestamp.valueOf(entity.getCreatedAt()));
                historyStatement.executeUpdate();

                connection.commit();
                return entity;
            } catch (SQLException exception) {
                rollback(connection);
                throw new DatabaseException("Failed to save quantity measurement", exception);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException exception) {
            throw new DatabaseException("Failed to manage database transaction", exception);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        return queryMeasurements("SELECT * FROM quantity_measurements ORDER BY created_at");
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByOperation(String operationType) {
        return queryMeasurements(
                "SELECT * FROM quantity_measurements WHERE UPPER(operation_type) = ? ORDER BY created_at",
                normalize(operationType));
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByMeasurementType(String measurementType) {
        return queryMeasurements(
                "SELECT * FROM quantity_measurements WHERE UPPER(left_measurement_type) = ? ORDER BY created_at",
                normalize(measurementType));
    }

    @Override
    public int getTotalCount() {
        return queryCount("SELECT COUNT(*) FROM quantity_measurements");
    }

    @Override
    public void deleteAllMeasurements() {
        executeUpdate("DELETE FROM quantity_measurement_history");
        executeUpdate("DELETE FROM quantity_measurements");
    }

    @Override
    public String getPoolStatistics() {
        return connectionPool.getStatistics();
    }

    @Override
    public void releaseResources() {
        connectionPool.close();
    }

    private void initializeSchema(String schemaLocation) {
        String schemaScript = readSchema(schemaLocation);
        Connection connection = connectionPool.acquireConnection();
        try (Statement statement = connection.createStatement()) {
            for (String sql : schemaScript.split(";")) {
                String trimmedSql = sql.trim();
                if (!trimmedSql.isEmpty()) {
                    statement.execute(trimmedSql);
                }
            }
        } catch (SQLException exception) {
            throw new DatabaseException("Failed to initialize database schema", exception);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    private String readSchema(String schemaLocation) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(schemaLocation)) {
            if (inputStream == null) {
                throw new DatabaseException("Schema file not found: " + schemaLocation);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new DatabaseException("Failed to read schema file", exception);
        }
    }

    private List<QuantityMeasurementEntity> queryMeasurements(String sql, Object... parameters) {
        Connection connection = connectionPool.acquireConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            bindParameters(statement, parameters);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<QuantityMeasurementEntity> measurements = new ArrayList<>();
                while (resultSet.next()) {
                    measurements.add(mapEntity(resultSet));
                }
                return measurements;
            }
        } catch (SQLException exception) {
            throw new DatabaseException("Failed to query quantity measurements", exception);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    private int queryCount(String sql) {
        Connection connection = connectionPool.acquireConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException exception) {
            throw new DatabaseException("Failed to count quantity measurements", exception);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    private void executeUpdate(String sql) {
        Connection connection = connectionPool.acquireConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DatabaseException("Failed to execute database update", exception);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    private void bindMeasurement(PreparedStatement statement, QuantityMeasurementEntity entity) throws SQLException {
        statement.setString(1, entity.getId());
        statement.setTimestamp(2, Timestamp.valueOf(entity.getCreatedAt()));
        statement.setString(3, entity.getOperationType());
        bindQuantity(statement, 4, entity.getLeftOperand());
        bindQuantity(statement, 7, entity.getRightOperand());
        bindQuantity(statement, 10, entity.getResultQuantity());
        bindNullableBoolean(statement, 13, entity.getComparisonResult());
        bindNullableDouble(statement, 14, entity.getScalarResult());
        statement.setBoolean(15, entity.hasError());
        statement.setString(16, entity.getErrorMessage());
    }

    private void bindQuantity(PreparedStatement statement, int startIndex, QuantityDTO quantityDTO) throws SQLException {
        if (quantityDTO == null) {
            statement.setObject(startIndex, null);
            statement.setString(startIndex + 1, null);
            statement.setString(startIndex + 2, null);
            return;
        }
        statement.setDouble(startIndex, quantityDTO.getValue());
        statement.setString(startIndex + 1, quantityDTO.getUnitName());
        statement.setString(startIndex + 2, quantityDTO.getMeasurementType());
    }

    private void bindNullableBoolean(PreparedStatement statement, int index, Boolean value) throws SQLException {
        if (value == null) {
            statement.setObject(index, null);
            return;
        }
        statement.setBoolean(index, value);
    }

    private void bindNullableDouble(PreparedStatement statement, int index, Double value) throws SQLException {
        if (value == null) {
            statement.setObject(index, null);
            return;
        }
        statement.setDouble(index, value);
    }

    private void bindParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int index = 0; index < parameters.length; index++) {
            statement.setObject(index + 1, parameters[index]);
        }
    }

    private QuantityMeasurementEntity mapEntity(ResultSet resultSet) throws SQLException {
        return QuantityMeasurementEntity.restore(
                resultSet.getString("id"),
                resultSet.getTimestamp("created_at").toLocalDateTime(),
                resultSet.getString("operation_type"),
                mapQuantity(resultSet, "left"),
                mapQuantity(resultSet, "right"),
                mapQuantity(resultSet, "result"),
                getNullableBoolean(resultSet, "comparison_result"),
                getNullableDouble(resultSet, "scalar_result"),
                resultSet.getBoolean("error"),
                resultSet.getString("error_message"));
    }

    private QuantityDTO mapQuantity(ResultSet resultSet, String prefix) throws SQLException {
        String unitName = resultSet.getString(prefix + "_unit_name");
        String measurementType = resultSet.getString(prefix + "_measurement_type");
        double value = resultSet.getDouble(prefix + "_value");
        if (unitName == null || measurementType == null) {
            return null;
        }
        return new QuantityDTO(value, unitName, measurementType);
    }

    private Boolean getNullableBoolean(ResultSet resultSet, String columnName) throws SQLException {
        boolean value = resultSet.getBoolean(columnName);
        return resultSet.wasNull() ? null : value;
    }

    private Double getNullableDouble(ResultSet resultSet, String columnName) throws SQLException {
        double value = resultSet.getDouble(columnName);
        return resultSet.wasNull() ? null : value;
    }

    private void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException exception) {
            LOGGER.warn("Failed to rollback transaction", exception);
        }
    }

    private String normalize(String value) {
        return Objects.requireNonNull(value, "Value must not be null").trim().toUpperCase(Locale.ROOT);
    }
}
