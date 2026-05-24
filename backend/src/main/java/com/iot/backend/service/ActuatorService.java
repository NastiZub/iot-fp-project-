package com.iot.backend.service;

import com.iot.backend.model.Actuator;
import com.iot.backend.repository.ActuatorRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ActuatorService {
    private final ActuatorRepository repository;

    public ActuatorService(ActuatorRepository repository) {
        this.repository = repository;
    }

    public List<Actuator> getAll() {
        return repository.findAll();
    }

    public Actuator getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Actuator save(Actuator item) {
        return repository.save(item);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
