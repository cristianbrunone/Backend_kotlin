package com.example.application.request

import kotlinx.serialization.Serializable

@Serializable
data class VerificationRequest(
    val userId: String 
)