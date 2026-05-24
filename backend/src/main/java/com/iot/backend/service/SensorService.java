package com.iot.backend.service;

import com.iot.backend.model.Sensor;
import com.iot.backend.repository.SensorRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SensorService {
    private final SensorRepository repository;

    public SensorService(SensorRepository repository) {
        this.repository = repository;
    }

    public List<Sensor> getAll() {
        return repository.findAll();
    }

    public Sensor getById(Integer id) {
        return repository.findById(id).orElseThrow();
    }

    public Sensor save(Sensor item) {
        return repository.save(item);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
