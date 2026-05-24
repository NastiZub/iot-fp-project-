package com.example.iotapp.model

data class MeasurementRequest(
    val sensorId: Long,
    val value: Double,
    val userId: Int,
    val sessionId: Int
)