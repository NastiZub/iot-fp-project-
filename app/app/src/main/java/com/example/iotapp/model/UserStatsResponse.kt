package com.example.iotapp.model

data class UserStatsResponse(
    val userId: Int,
    val username: String,
    val email: String,
    val role: String,
    val createdAt: String,
    val measurementsCount: Long,
    val rfidTagsCount: Long
)