package com.iot.backend.controller;

import com.iot.backend.model.Device;
import com.iot.backend.service.DeviceService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/devices")
@CrossOrigin(origins = "*")
public class DeviceController {
    private final DeviceService service;

    public DeviceController(DeviceService service) {
        this.service = service;
    }

    @GetMapping
    public List<Device> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Device getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public Device create(@RequestBody Device item) {
        return service.save(item);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
