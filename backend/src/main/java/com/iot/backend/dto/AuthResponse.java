package com.iot.backend.dto;

public class AuthResponse {

    private Integer userId;
    private String username;
    private String role;
    private boolean success;
    private String message;

    public AuthResponse() {}

    public AuthResponse(Integer userId, String username, String role, boolean success, String message) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.success = success;
        this.message = message;
    }

    public Integer getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }

    public void setUserId(Integer userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }
    public void setRole(String role) { this.role = role; }
    public void setSuccess(boolean success) { this.success = success; }
    public void setMessage(String message) { this.message = message; }
}