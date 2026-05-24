package com.example.iotapp.model

data class AuthResponse(
    val userId: Int?,
    val username: String?,
    val role: String?,
    val success: Boolean,
    val message: String
)