package com.iot.backend.repository;

import com.iot.backend.model.MeasurementSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeasurementSessionRepository
        extends JpaRepository<MeasurementSession, Integer> {

    List<MeasurementSession> findByUserIdOrderByCreatedAtDesc(Integer userId);
}