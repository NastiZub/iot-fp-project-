package com.iot.backend.dto;

public class CreateMeasurementSessionRequest {

    private Integer userId;
    private String mode;
    private String scope;

    public CreateMeasurementSessionRequest() {}

    public Integer getUserId() { return userId; }
    public String getMode() { return mode; }
    public String getScope() { return scope; }

    public void setUserId(Integer userId) { this.userId = userId; }
    public void setMode(String mode) { this.mode = mode; }
    public void setScope(String scope) { this.scope = scope; }
}