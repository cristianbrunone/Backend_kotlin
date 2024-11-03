package com.example.application.response

import kotlinx.serialization.Serializable

@Serializable
data class VerificationResponse(
    val success: Boolean,
    val redirectUrl: String?
)
