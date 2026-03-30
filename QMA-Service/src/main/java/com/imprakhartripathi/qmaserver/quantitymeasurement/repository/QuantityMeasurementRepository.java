package com.imprakhartripathi.qmaserver.quantitymeasurement.repository;

import com.imprakhartripathi.qmaserver.quantitymeasurement.model.OperationType;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuantityMeasurementRepository extends JpaRepository<QuantityMeasurementEntity, Long> {
    List<QuantityMeasurementEntity> findByOperationTypeOrderByCreatedAtAsc(OperationType operationType);

    List<QuantityMeasurementEntity> findByLeftMeasurementTypeOrderByCreatedAtAsc(String measurementType);

    List<QuantityMeasurementEntity> findByCreatedAtAfter(LocalDateTime createdAt);

    @Query("select q from QuantityMeasurementEntity q where q.operationType = :operationType and q.error = false order by q.createdAt asc")
    List<QuantityMeasurementEntity> findSuccessfulByOperationType(OperationType operationType);

    long countByOperationTypeAndErrorFalse(OperationType operationType);

    List<QuantityMeasurementEntity> findByErrorTrueOrderByCreatedAtAsc();

    List<QuantityMeasurementEntity> findByUserEmailIgnoreCaseOrderByCreatedAtDesc(String userEmail);
}
