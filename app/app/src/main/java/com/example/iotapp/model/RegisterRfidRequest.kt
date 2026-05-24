package com.example.iotapp.model

data class RegisterRfidRequest(
    val userId: Int,
    val tagUid: String,
    val tagName: String
)