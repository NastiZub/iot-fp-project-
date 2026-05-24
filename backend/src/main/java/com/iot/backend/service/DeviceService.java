package com.iot.backend.service;

import com.iot.backend.model.Device;
import com.iot.backend.repository.DeviceRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DeviceService {
    private final DeviceRepository repository;

    public DeviceService(DeviceRepository repository) {
        this.repository = repository;
    }

    public List<Device> getAll() {
        return repository.findAll();
    }

    public Device getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Device save(Device item) {
        return repository.save(item);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
