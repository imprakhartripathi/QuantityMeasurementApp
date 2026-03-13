package com.imprakhartripathi.qmaserver.quantitymeasurement.util;

import com.imprakhartripathi.qmaserver.quantitymeasurement.exception.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Deque;

public final class ConnectionPool {
    private final ApplicationConfig config;
    private final Deque<Connection> idleConnections = new ArrayDeque<>();
    private int totalConnections;

    public ConnectionPool(ApplicationConfig config) {
        this.config = config;
        for (int index = 0; index < config.getInitialPoolSize(); index++) {
            idleConnections.add(createConnection());
            totalConnections++;
        }
    }

    public synchronized Connection acquireConnection() {
        long deadline = System.currentTimeMillis() + config.getConnectionTimeoutMillis();
        while (idleConnections.isEmpty() && totalConnections >= config.getMaxPoolSize()) {
            long remaining = deadline - System.currentTimeMillis();
            if (remaining <= 0) {
                throw new DatabaseException("Timed out waiting for a database connection");
            }
            try {
                wait(remaining);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new DatabaseException("Interrupted while waiting for a database connection", exception);
            }
        }

        if (!idleConnections.isEmpty()) {
            return idleConnections.removeFirst();
        }

        Connection connection = createConnection();
        totalConnections++;
        return connection;
    }

    public synchronized void releaseConnection(Connection connection) {
        if (connection == null) {
            return;
        }
        try {
            if (connection.isClosed() || !connection.isValid(2)) {
                totalConnections--;
            } else {
                idleConnections.addLast(connection);
            }
        } catch (SQLException exception) {
            totalConnections--;
            quietlyClose(connection);
        } finally {
            notifyAll();
        }
    }

    public synchronized String getStatistics() {
        int idle = idleConnections.size();
        int active = totalConnections - idle;
        return "total=" + totalConnections + ", active=" + active + ", idle=" + idle;
    }

    public synchronized void close() {
        while (!idleConnections.isEmpty()) {
            quietlyClose(idleConnections.removeFirst());
        }
        totalConnections = 0;
    }

    private Connection createConnection() {
        try {
            return DriverManager.getConnection(
                    config.getDatabaseUrl(),
                    config.getDatabaseUsername(),
                    config.getDatabasePassword());
        } catch (SQLException exception) {
            throw new DatabaseException("Failed to create database connection", exception);
        }
    }

    private void quietlyClose(Connection connection) {
        try {
            connection.close();
        } catch (SQLException ignored) {
        }
    }
}
