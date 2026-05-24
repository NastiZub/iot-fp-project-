package com.iot.backend.controller;

import com.iot.backend.dto.SensorMeasurementRequest;
import com.iot.backend.dto.SensorMeasurementResponse;
import com.iot.backend.model.Measurement;
import com.iot.backend.service.MeasurementService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.iot.backend.dto.MeasurementHistoryResponse;

@RestController
@RequestMapping("/api/measurements")
@CrossOrigin(origins = "*")
public class MeasurementController {
    private final MeasurementService service;

    public MeasurementController(MeasurementService service) {
        this.service = service;
    }

    @GetMapping
    public List<Measurement> getAll() {
        return service.getAll();
    }

    @PostMapping
    public SensorMeasurementResponse create(@RequestBody SensorMeasurementRequest request) {
        return service.saveMeasurement(request);
    }
    @GetMapping("/session/{sessionId}")
    public List<MeasurementHistoryResponse> getMeasurementsBySession(
            @PathVariable Integer sessionId
    ) {
        return service.getMeasurementsBySession(sessionId);
    }
}
