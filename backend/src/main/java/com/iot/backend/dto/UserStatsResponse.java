package com.iot.backend.dto;

import java.time.LocalDateTime;

public class UserStatsResponse {

    private Integer userId;
    private String username;
    private String email;
    private String role;
    private LocalDateTime createdAt;
    private Long measurementsCount;
    private Long rfidTagsCount;

    public UserStatsResponse() {}

    public UserStatsResponse(
            Integer userId,
            String username,
            String email,
            String role,
            LocalDateTime createdAt,
            Long measurementsCount,
            Long rfidTagsCount
    ) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
        this.measurementsCount = measurementsCount;
        this.rfidTagsCount = rfidTagsCount;
    }

    public Integer getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Long getMeasurementsCount() { return measurementsCount; }
    public Long getRfidTagsCount() { return rfidTagsCount; }

    public void setUserId(Integer userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(String role) { this.role = role; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setMeasurementsCount(Long measurementsCount) { this.measurementsCount = measurementsCount; }
    public void setRfidTagsCount(Long rfidTagsCount) { this.rfidTagsCount = rfidTagsCount; }
}