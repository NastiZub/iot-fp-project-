package com.example.iotapp.model

data class MeasurementResponse(
    val id: Long,
    val sensorId: Long,
    val value: Double,
    val createdAt: String
)