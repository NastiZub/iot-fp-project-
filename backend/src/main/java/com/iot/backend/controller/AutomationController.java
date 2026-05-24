package com.iot.backend.controller;

import com.iot.backend.model.AutomationRule;
import com.iot.backend.service.AutomationService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/automations")
@CrossOrigin(origins = "*")
public class AutomationController {
    private final AutomationService service;

    public AutomationController(AutomationService service) {
        this.service = service;
    }

    @GetMapping
    public List<AutomationRule> getAll() {
        return service.getAll();
    }

    @PostMapping
    public AutomationRule create(@RequestBody AutomationRule item) {
        return service.save(item);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
