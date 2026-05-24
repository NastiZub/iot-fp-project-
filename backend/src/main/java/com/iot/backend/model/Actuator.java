package com.iot.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "actuators")
public class Actuator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "actuator_type")
    private String actuatorType;

    private String state;

    @Column(name = "is_active")
    private boolean active;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    public Actuator() {}

    public Integer getId() { return id; }
    public String getName() { return name; }
    public String getActuatorType() { return actuatorType; }
    public String getState() { return state; }
    public boolean isActive() { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Device getDevice() { return device; }

    public void setId(Integer id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setActuatorType(String actuatorType) { this.actuatorType = actuatorType; }
    public void setState(String state) { this.state = state; }
    public void setActive(boolean active) { this.active = active; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setDevice(Device device) { this.device = device; }
}