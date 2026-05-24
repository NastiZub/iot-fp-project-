package com.iot.backend.service;

import com.iot.backend.dto.CreateMeasurementSessionRequest;
import com.iot.backend.dto.MeasurementSessionResponse;
import com.iot.backend.model.MeasurementSession;
import com.iot.backend.model.User;
import com.iot.backend.repository.MeasurementSessionRepository;
import com.iot.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeasurementSessionService {

    private final MeasurementSessionRepository sessionRepository;
    private final UserRepository userRepository;

    public MeasurementSessionService(
            MeasurementSessionRepository sessionRepository,
            UserRepository userRepository
    ) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    public MeasurementSessionResponse createSession(CreateMeasurementSessionRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow();

        MeasurementSession session = new MeasurementSession();
        session.setUser(user);
        session.setMode(request.getMode());
        session.setScope(request.getScope());
        session.setCreatedAt(LocalDateTime.now());

        MeasurementSession saved = sessionRepository.save(session);

        return new MeasurementSessionResponse(
                saved.getId(),
                saved.getMode(),
                saved.getScope(),
                saved.getCreatedAt()
        );
    }

    public List<MeasurementSessionResponse> getSessionsByUser(Integer userId) {
        return sessionRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(session -> new MeasurementSessionResponse(
                        session.getId(),
                        session.getMode(),
                        session.getScope(),
                        session.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}