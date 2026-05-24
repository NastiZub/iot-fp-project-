package com.iot.backend.repository;

import com.iot.backend.model.Actuator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActuatorRepository extends JpaRepository<Actuator, Long> {
}
