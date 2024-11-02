package com.example.application.response

data class TokenResponse(
    val id: String, // ID del token en la base de datos
    val userId: String, // ID del usuario asociado
    val token: String // Token de Firebase
)
