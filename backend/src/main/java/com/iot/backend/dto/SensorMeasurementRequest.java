package com.iot.backend.dto;

public class SensorMeasurementRequest {

    private Integer sensorId;
    private double value;
    private Integer userId;
    private Integer sessionId;

    public SensorMeasurementRequest() {}

    public Integer getSensorId() {
        return sensorId;
    }

    public double getValue() {
        return value;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSensorId(Integer sensorId) {
        this.sensorId = sensorId;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }
}