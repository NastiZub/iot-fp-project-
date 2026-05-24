package com.iot.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sensors")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "is_active")
    private boolean active;

    @Column(name = "connection_info")
    private String connectionInfo;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    @ManyToOne
    @JoinColumn(name = "sensor_type_id")
    private SensorType sensorType;

    public Sensor() {}

    public Integer getId() { return id; }
    public String getName() { return name; }
    public boolean isActive() { return active; }
    public String getConnectionInfo() { return connectionInfo; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Device getDevice() { return device; }
    public SensorType getSensorType() { return sensorType; }

    public void setId(Integer id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setActive(boolean active) { this.active = active; }
    public void setConnectionInfo(String connectionInfo) { this.connectionInfo = connectionInfo; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setDevice(Device device) { this.device = device; }
    public void setSensorType(SensorType sensorType) { this.sensorType = sensorType; }
}