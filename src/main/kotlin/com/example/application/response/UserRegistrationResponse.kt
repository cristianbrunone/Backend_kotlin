package com.example.application.response

import kotlinx.serialization.Serializable

@Serializable
data class UserRegistrationResponse(
    val success: Boolean,
    val userId: String? = null,
    val message: String
)
