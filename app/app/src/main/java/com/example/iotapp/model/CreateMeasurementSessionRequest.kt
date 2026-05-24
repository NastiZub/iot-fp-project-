package com.example.iotapp.model

data class CreateMeasurementSessionRequest(
    val userId: Int,
    val mode: String,
    val scope: String
)