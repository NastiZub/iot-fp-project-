package com.example.iotapp.model

data class MeasurementHistoryResponse(
    val id: Int,
    val sensorName: String,
    val unit: String,
    val value: Double,
    val measuredAt: String
)