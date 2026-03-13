package com.imprakhartripathi.qmaserver.quantitymeasurement.app;

import com.imprakhartripathi.qmaserver.quantitymeasurement.controller.QuantityMeasurementController;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityDTO;
import com.imprakhartripathi.qmaserver.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.imprakhartripathi.qmaserver.quantitymeasurement.repository.QuantityMeasurementCacheRepository;
import com.imprakhartripathi.qmaserver.quantitymeasurement.service.IQuantityMeasurementService;
import com.imprakhartripathi.qmaserver.quantitymeasurement.service.QuantityMeasurementServiceImpl;

public final class QuantityMeasurementApp {
    private QuantityMeasurementApp() {
    }

    public static QuantityMeasurementController createController() {
        IQuantityMeasurementRepository repository = QuantityMeasurementCacheRepository.getInstance();
        IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repository);
        return new QuantityMeasurementController(service);
    }

    public static void main(String[] args) {
        QuantityMeasurementController controller = createController();
        QuantityDTO left = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO right = new QuantityDTO(12.0, "INCH", "LENGTH");
        System.out.println(controller.displayResult(controller.performComparison(left, right)));
    }
}
