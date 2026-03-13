package com.imprakhartripathi.qmaserver.quantitymeasurement.app;

import com.imprakhartripathi.qmaserver.quantitymeasurement.controller.QuantityMeasurementController;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityDTO;
import com.imprakhartripathi.qmaserver.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.imprakhartripathi.qmaserver.quantitymeasurement.repository.QuantityMeasurementRepositoryFactory;
import com.imprakhartripathi.qmaserver.quantitymeasurement.service.IQuantityMeasurementService;
import com.imprakhartripathi.qmaserver.quantitymeasurement.service.QuantityMeasurementServiceImpl;
import com.imprakhartripathi.qmaserver.quantitymeasurement.util.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class QuantityMeasurementApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuantityMeasurementApp.class);

    private QuantityMeasurementApp() {
    }

    public static QuantityMeasurementController createController() {
        return createController(ApplicationConfig.load());
    }

    public static QuantityMeasurementController createController(ApplicationConfig config) {
        IQuantityMeasurementRepository repository = QuantityMeasurementRepositoryFactory.create(config);
        IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repository);
        return new QuantityMeasurementController(service);
    }

    public static IQuantityMeasurementRepository createRepository(ApplicationConfig config) {
        return QuantityMeasurementRepositoryFactory.create(config);
    }

    public static void closeResources(IQuantityMeasurementRepository repository) {
        repository.releaseResources();
    }

    public static void deleteAllMeasurements(IQuantityMeasurementRepository repository) {
        repository.deleteAllMeasurements();
    }

    public static void main(String[] args) {
        ApplicationConfig config = ApplicationConfig.load();
        IQuantityMeasurementRepository repository = createRepository(config);
        QuantityMeasurementController controller = new QuantityMeasurementController(
                new QuantityMeasurementServiceImpl(repository));
        try {
            QuantityDTO left = new QuantityDTO(1.0, "FEET", "LENGTH");
            QuantityDTO right = new QuantityDTO(12.0, "INCH", "LENGTH");
            LOGGER.info("Repository type: {}", config.getRepositoryType());
            LOGGER.info(controller.displayResult(controller.performComparison(left, right)));
            LOGGER.info("Stored measurements: {}", repository.getTotalCount());
            LOGGER.info("Repository stats: {}", repository.getPoolStatistics());
        } finally {
            closeResources(repository);
        }
    }
}
