package com.iot.backend.controller;

import com.iot.backend.dto.CreateMeasurementSessionRequest;
import com.iot.backend.dto.MeasurementSessionResponse;
import com.iot.backend.service.MeasurementSessionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/measurement-sessions")
@CrossOrigin(origins = "*")
public class MeasurementSessionController {

    private final MeasurementSessionService sessionService;

    public MeasurementSessionController(
            MeasurementSessionService sessionService
    ) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public MeasurementSessionResponse createSession(
            @RequestBody CreateMeasurementSessionRequest request
    ) {
        return sessionService.createSession(request);
    }

    @GetMapping("/user/{userId}")
    public List<MeasurementSessionResponse> getSessionsByUser(
            @PathVariable Integer userId
    ) {
        return sessionService.getSessionsByUser(userId);
    }
}