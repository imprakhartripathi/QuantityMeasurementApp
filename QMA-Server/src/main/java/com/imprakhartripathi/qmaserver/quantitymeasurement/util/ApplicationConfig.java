package com.imprakhartripathi.qmaserver.quantitymeasurement.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public final class ApplicationConfig {
    private static final String CONFIG_FILE = "application.properties";

    private final String repositoryType;
    private final String databaseUrl;
    private final String databaseUsername;
    private final String databasePassword;
    private final int initialPoolSize;
    private final int maxPoolSize;
    private final long connectionTimeoutMillis;
    private final String schemaLocation;

    private ApplicationConfig(Properties properties) {
        this.repositoryType = read(properties, "quantity.measurement.repository.type", "database");
        this.databaseUrl = read(properties, "quantity.measurement.database.url",
                "jdbc:postgresql://127.0.0.1:5432/quantity_measurement");
        this.databaseUsername = read(properties, "quantity.measurement.database.username", "postgres");
        this.databasePassword = read(properties, "quantity.measurement.database.password", "");
        this.initialPoolSize = Integer.parseInt(read(properties,
                "quantity.measurement.database.pool.initial-size", "1"));
        this.maxPoolSize = Integer.parseInt(read(properties,
                "quantity.measurement.database.pool.max-size", "5"));
        this.connectionTimeoutMillis = Long.parseLong(read(properties,
                "quantity.measurement.database.pool.connection-timeout-millis", "5000"));
        this.schemaLocation = read(properties, "quantity.measurement.database.schema-location", "db/schema.sql");
    }

    public static ApplicationConfig load() {
        Properties properties = new Properties();
        try (InputStream inputStream = ApplicationConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load application configuration", exception);
        }
        return new ApplicationConfig(properties);
    }

    private String read(Properties properties, String key, String defaultValue) {
        String value = System.getProperty(key);
        if (value != null && !value.isBlank()) {
            return value;
        }
        return properties.getProperty(key, defaultValue);
    }

    public String getRepositoryType() {
        return repositoryType;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public int getInitialPoolSize() {
        return initialPoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public long getConnectionTimeoutMillis() {
        return connectionTimeoutMillis;
    }

    public String getSchemaLocation() {
        return schemaLocation;
    }

    public boolean useDatabaseRepository() {
        return Objects.equals("database", repositoryType.trim().toLowerCase());
    }
}
