package com.iot.backend.repository;

import com.iot.backend.model.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {

    Long countByUserId(Integer userId);

    List<Measurement> findBySessionIdOrderByMeasuredAtAsc(Integer sessionId);
}