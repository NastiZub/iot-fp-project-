package com.iot.backend.controller;

import com.iot.backend.model.Sensor;
import com.iot.backend.service.SensorService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sensors")
@CrossOrigin(origins = "*")
public class SensorController {
    private final SensorService service;

    public SensorController(SensorService service) {
        this.service = service;
    }

    @GetMapping
    public List<Sensor> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Sensor getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @PostMapping
    public Sensor create(@RequestBody Sensor item) {
        return service.save(item);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
