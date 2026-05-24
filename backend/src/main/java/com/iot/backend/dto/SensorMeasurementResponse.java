package com.iot.backend.dto;

import java.time.LocalDateTime;

public class SensorMeasurementResponse {

    private Integer id;
    private Integer sensorId;
    private double value;
    private LocalDateTime measuredAt;

    public SensorMeasurementResponse() {}

    public SensorMeasurementResponse(Integer id, Integer sensorId, double value, LocalDateTime measuredAt) {
        this.id = id;
        this.sensorId = sensorId;
        this.value = value;
        this.measuredAt = measuredAt;
    }

    public Integer getId() { return id; }
    public Integer getSensorId() { return sensorId; }
    public double getValue() { return value; }
    public LocalDateTime getMeasuredAt() { return measuredAt; }

    public void setId(Integer id) { this.id = id; }
    public void setSensorId(Integer sensorId) { this.sensorId = sensorId; }
    public void setValue(double value) { this.value = value; }
    public void setMeasuredAt(LocalDateTime measuredAt) { this.measuredAt = measuredAt; }
}
