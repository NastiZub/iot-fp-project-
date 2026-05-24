package com.example.iotapp.model

data class MeasurementSessionResponse(
    val id: Int,
    val mode: String,
    val scope: String,
    val createdAt: String
)