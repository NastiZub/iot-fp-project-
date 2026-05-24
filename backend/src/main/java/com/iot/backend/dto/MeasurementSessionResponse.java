package com.iot.backend.dto;

import java.time.LocalDateTime;

public class MeasurementSessionResponse {

    private Integer id;
    private String mode;
    private String scope;
    private LocalDateTime createdAt;

    public MeasurementSessionResponse() {}

    public MeasurementSessionResponse(Integer id, String mode, String scope, LocalDateTime createdAt) {
        this.id = id;
        this.mode = mode;
        this.scope = scope;
        this.createdAt = createdAt;
    }

    public Integer getId() { return id; }
    public String getMode() { return mode; }
    public String getScope() { return scope; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Integer id) { this.id = id; }
    public void setMode(String mode) { this.mode = mode; }
    public void setScope(String scope) { this.scope = scope; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}