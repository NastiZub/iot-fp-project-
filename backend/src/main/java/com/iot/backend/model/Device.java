package com.iot.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "device_type")
    private String deviceType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Device() {}

    public Integer getId() { return id; }
    public String getName() { return name; }
    public String getDeviceType() { return deviceType; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Integer id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
