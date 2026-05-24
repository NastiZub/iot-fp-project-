package com.iot.backend.repository;

import com.iot.backend.model.CameraEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CameraEventRepository extends JpaRepository<CameraEvent, Long> {
}
