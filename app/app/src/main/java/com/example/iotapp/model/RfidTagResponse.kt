package com.example.iotapp.model

data class RfidTagResponse(
    val id: Int,
    val tagUid: String,
    val tagName: String,
    val createdAt: String
)