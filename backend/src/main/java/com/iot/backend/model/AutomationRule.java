package com.iot.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "automation_rules")
public class AutomationRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "condition_operator")
    private String conditionOperator;

    @Column(name = "condition_value")
    private String conditionValue;

    @Column(name = "action_type")
    private String actionType;

    @Column(name = "is_enabled")
    private boolean enabled;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "source_sensor_id")
    private Sensor sourceSensor;

    @ManyToOne
    @JoinColumn(name = "target_sensor_id")
    private Sensor targetSensor;

    public AutomationRule() {}

    public Integer getId() { return id; }
    public String getName() { return name; }
    public String getConditionOperator() { return conditionOperator; }
    public String getConditionValue() { return conditionValue; }
    public String getActionType() { return actionType; }
    public boolean isEnabled() { return enabled; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Sensor getSourceSensor() { return sourceSensor; }
    public Sensor getTargetSensor() { return targetSensor; }

    public void setId(Integer id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setConditionOperator(String conditionOperator) { this.conditionOperator = conditionOperator; }
    public void setConditionValue(String conditionValue) { this.conditionValue = conditionValue; }
    public void setActionType(String actionType) { this.actionType = actionType; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setSourceSensor(Sensor sourceSensor) { this.sourceSensor = sourceSensor; }
    public void setTargetSensor(Sensor targetSensor) { this.targetSensor = targetSensor; }
}