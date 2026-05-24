package com.iot.backend.service;

import com.iot.backend.dto.UserStatsResponse;
import com.iot.backend.model.User;
import com.iot.backend.repository.MeasurementRepository;
import com.iot.backend.repository.RfidTagRepository;
import com.iot.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository repository;
    private final MeasurementRepository measurementRepository;
    private final RfidTagRepository rfidTagRepository;

    public UserService(
            UserRepository repository,
            MeasurementRepository measurementRepository,
            RfidTagRepository rfidTagRepository
    ) {
        this.repository = repository;
        this.measurementRepository = measurementRepository;
        this.rfidTagRepository = rfidTagRepository;
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    public User getById(Integer id) {
        return repository.findById(id).orElseThrow();
    }

    public User save(User item) {
        return repository.save(item);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<UserStatsResponse> getUsersStats() {
        return repository.findAll()
                .stream()
                .map(user -> new UserStatsResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole(),
                        user.getCreatedAt(),
                        measurementRepository.countByUserId(user.getId()),
                        rfidTagRepository.countByUserId(user.getId())
                ))
                .collect(Collectors.toList());
    }
}
