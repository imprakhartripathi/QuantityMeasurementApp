package com.imprakhartripathi.qmaserver.quantitymeasurement.repository;

import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityMeasurementEntity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class QuantityMeasurementCacheRepository implements IQuantityMeasurementRepository, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final Path STORAGE_FILE = Path.of(System.getProperty("java.io.tmpdir"),
            "quantity-measurement-cache.ser");
    private static final QuantityMeasurementCacheRepository INSTANCE = new QuantityMeasurementCacheRepository();

    private final List<QuantityMeasurementEntity> measurements;

    private QuantityMeasurementCacheRepository() {
        this.measurements = new ArrayList<>(loadFromDisk());
    }

    public static QuantityMeasurementCacheRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public synchronized QuantityMeasurementEntity save(QuantityMeasurementEntity entity) {
        measurements.add(entity);
        persist();
        return entity;
    }

    @Override
    public synchronized List<QuantityMeasurementEntity> getAllMeasurements() {
        return Collections.unmodifiableList(new ArrayList<>(measurements));
    }

    @Override
    public synchronized void clear() {
        measurements.clear();
        persist();
    }

    @SuppressWarnings("unchecked")
    private List<QuantityMeasurementEntity> loadFromDisk() {
        if (!Files.exists(STORAGE_FILE)) {
            return List.of();
        }
        try (ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(STORAGE_FILE))) {
            Object content = inputStream.readObject();
            if (content instanceof List<?>) {
                return (List<QuantityMeasurementEntity>) content;
            }
            return List.of();
        } catch (IOException | ClassNotFoundException exception) {
            return List.of();
        }
    }

    private void persist() {
        try {
            Files.createDirectories(STORAGE_FILE.getParent());
            try (ObjectOutputStream outputStream = new ObjectOutputStream(Files.newOutputStream(STORAGE_FILE))) {
                outputStream.writeObject(measurements);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to persist quantity measurements", exception);
        }
    }
}
