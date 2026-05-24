package com.iot.backend.controller;

import com.iot.backend.model.Actuator;
import com.iot.backend.service.ActuatorService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/actuators")
@CrossOrigin(origins = "*")
public class ActuatorController {
    private final ActuatorService service;

    public ActuatorController(ActuatorService service) {
        this.service = service;
    }

    @GetMapping
    public List<Actuator> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Actuator getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public Actuator create(@RequestBody Actuator item) {
        return service.save(item);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
