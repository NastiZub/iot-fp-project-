package com.iot.backend.service;

import com.iot.backend.model.AutomationRule;
import com.iot.backend.repository.AutomationRuleRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AutomationService {
    private final AutomationRuleRepository repository;

    public AutomationService(AutomationRuleRepository repository) {
        this.repository = repository;
    }

    public List<AutomationRule> getAll() {
        return repository.findAll();
    }

    public AutomationRule getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public AutomationRule save(AutomationRule item) {
        return repository.save(item);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
