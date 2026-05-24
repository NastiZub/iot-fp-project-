package com.iot.backend.repository;

import com.iot.backend.model.AutomationRule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutomationRuleRepository extends JpaRepository<AutomationRule, Long> {
}
