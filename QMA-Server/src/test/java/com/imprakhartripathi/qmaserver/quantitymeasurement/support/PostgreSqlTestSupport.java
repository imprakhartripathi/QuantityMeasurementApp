package com.imprakhartripathi.qmaserver.quantitymeasurement.support;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public final class PostgreSqlTestSupport {
    private static final String PG_BIN = "/usr/lib/postgresql/17/bin";
    private static final Path BASE_DIR = Path.of("target", "postgres-test");
    private static final Path DATA_DIR = BASE_DIR.resolve("data");
    private static final Path LOG_FILE = BASE_DIR.resolve("postgres.log");
    private static final Path SOCKET_DIR = Path.of("/tmp", "qma-pg-socket");
    private static final int PORT = 55432;
    private static final AtomicBoolean STARTED = new AtomicBoolean(false);

    private PostgreSqlTestSupport() {
    }

    public static synchronized void ensureDatabase(String databaseName) {
        try {
            Files.createDirectories(BASE_DIR);
            Files.createDirectories(SOCKET_DIR);
            if (!Files.exists(DATA_DIR.resolve("PG_VERSION"))) {
                runCommand(List.of(
                        PG_BIN + "/initdb",
                        "-D", DATA_DIR.toAbsolutePath().toString(),
                        "-A", "trust",
                        "-U", "postgres"));
            }
            if (STARTED.compareAndSet(false, true)) {
                runCommand(List.of(
                        PG_BIN + "/pg_ctl",
                        "-D", DATA_DIR.toAbsolutePath().toString(),
                        "-l", LOG_FILE.toAbsolutePath().toString(),
                        "-o", "-p " + PORT + " -k " + SOCKET_DIR.toAbsolutePath(),
                        "start",
                        "-w"));
                Runtime.getRuntime().addShutdownHook(new Thread(PostgreSqlTestSupport::stopQuietly));
            }
            runCommandAllowingFailure(List.of(
                    PG_BIN + "/createdb",
                    "-h", "127.0.0.1",
                    "-p", String.valueOf(PORT),
                    "-U", "postgres",
                    databaseName));
        } catch (IOException | InterruptedException exception) {
            throw new IllegalStateException("Failed to prepare PostgreSQL test database", exception);
        }
    }

    public static String jdbcUrl(String databaseName) {
        return "jdbc:postgresql://127.0.0.1:" + PORT + "/" + databaseName;
    }

    private static void stopQuietly() {
        try {
            runCommandAllowingFailure(List.of(
                    PG_BIN + "/pg_ctl",
                    "-D", DATA_DIR.toAbsolutePath().toString(),
                    "stop",
                    "-m", "fast"));
        } catch (IOException | InterruptedException ignored) {
        }
    }

    private static void runCommand(List<String> command) throws IOException, InterruptedException {
        Process process = new ProcessBuilder(command)
                .redirectErrorStream(true)
                .start();
        String output = new String(process.getInputStream().readAllBytes());
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IllegalStateException("Command failed: " + String.join(" ", command) + "\n" + output);
        }
    }

    private static void runCommandAllowingFailure(List<String> command) throws IOException, InterruptedException {
        Process process = new ProcessBuilder(command)
                .redirectErrorStream(true)
                .start();
        process.waitFor();
    }
}
