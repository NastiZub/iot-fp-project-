package com.iot.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "measurement_sessions")
public class MeasurementSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String mode;

    private String scope;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public MeasurementSession() {}

    public Integer getId() { return id; }
    public String getMode() { return mode; }
    public String getScope() { return scope; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public User getUser() { return user; }

    public void setId(Integer id) { this.id = id; }
    public void setMode(String mode) { this.mode = mode; }
    public void setScope(String scope) { this.scope = scope; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUser(User user) { this.user = user; }
}