package com.iot.backend.dto;

import java.time.LocalDateTime;

public class MeasurementHistoryResponse {

    private Integer id;
    private String sensorName;
    private String unit;
    private double value;
    private LocalDateTime measuredAt;

    public MeasurementHistoryResponse() {}

    public MeasurementHistoryResponse(
            Integer id,
            String sensorName,
            String unit,
            double value,
            LocalDateTime measuredAt
    ) {
        this.id = id;
        this.sensorName = sensorName;
        this.unit = unit;
        this.value = value;
        this.measuredAt = measuredAt;
    }

    public Integer getId() { return id; }
    public String getSensorName() { return sensorName; }
    public String getUnit() { return unit; }
    public double getValue() { return value; }
    public LocalDateTime getMeasuredAt() { return measuredAt; }

    public void setId(Integer id) { this.id = id; }
    public void setSensorName(String sensorName) { this.sensorName = sensorName; }
    public void setUnit(String unit) { this.unit = unit; }
    public void setValue(double value) { this.value = value; }
    public void setMeasuredAt(LocalDateTime measuredAt) { this.measuredAt = measuredAt; }
}