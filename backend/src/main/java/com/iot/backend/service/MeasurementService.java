package com.iot.backend.service;

import com.iot.backend.dto.SensorMeasurementRequest;
import com.iot.backend.dto.SensorMeasurementResponse;
import com.iot.backend.model.Measurement;
import com.iot.backend.model.Sensor;
import com.iot.backend.model.User;
import com.iot.backend.repository.MeasurementRepository;
import com.iot.backend.repository.SensorRepository;
import com.iot.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import com.iot.backend.model.MeasurementSession;
import com.iot.backend.repository.MeasurementSessionRepository;
import com.iot.backend.dto.MeasurementHistoryResponse;
import java.util.stream.Collectors;

@Service
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final SensorRepository sensorRepository;
    private final UserRepository userRepository;
    private final MeasurementSessionRepository sessionRepository;

    public MeasurementService(
            MeasurementRepository measurementRepository,
            SensorRepository sensorRepository,
            UserRepository userRepository,
            MeasurementSessionRepository sessionRepository
    ) {
        this.measurementRepository = measurementRepository;
        this.sensorRepository = sensorRepository;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    public SensorMeasurementResponse saveMeasurement(SensorMeasurementRequest request) {

        Sensor sensor = sensorRepository.findById(request.getSensorId()).orElseThrow();
        User user = userRepository.findById(request.getUserId()).orElseThrow();
        MeasurementSession session =
                sessionRepository.findById(request.getSessionId()).orElseThrow();

        Measurement measurement = new Measurement();
        measurement.setSensor(sensor);
        measurement.setUser(user);
        measurement.setSession(session);
        measurement.setValue(request.getValue());
        measurement.setMeasuredAt(LocalDateTime.now());

        Measurement saved = measurementRepository.save(measurement);

        return new SensorMeasurementResponse(
                saved.getId(),
                saved.getSensor().getId(),
                saved.getValue(),
                saved.getMeasuredAt()
        );
    }

    public List<Measurement> getAll() {
        return measurementRepository.findAll();
    }

    public List<MeasurementHistoryResponse> getMeasurementsBySession(Integer sessionId) {
        return measurementRepository.findBySessionIdOrderByMeasuredAtAsc(sessionId)
                .stream()
                .map(measurement -> new MeasurementHistoryResponse(
                        measurement.getId(),
                        measurement.getSensor().getName(),
                        measurement.getSensor().getSensorType().getUnit(),
                        measurement.getValue(),
                        measurement.getMeasuredAt()
                ))
                .collect(Collectors.toList());
    }
}