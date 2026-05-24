package com.iot.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "measurements")
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private double value;

    @Column(name = "measured_at")
    private LocalDateTime measuredAt;

    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private MeasurementSession session;

    public Measurement() {}

    public MeasurementSession getSession() {return session; }
    public Integer getId() { return id; }
    public double getValue() { return value; }
    public LocalDateTime getMeasuredAt() { return measuredAt; }
    public Sensor getSensor() { return sensor; }
    public User getUser() { return user; }

    public void setId(Integer id) { this.id = id; }
    public void setValue(double value) { this.value = value; }
    public void setMeasuredAt(LocalDateTime measuredAt) { this.measuredAt = measuredAt; }
    public void setSensor(Sensor sensor) { this.sensor = sensor; }
    public void setUser(User user) { this.user = user; }
    public void setSession(MeasurementSession session) {this.session = session; }
}