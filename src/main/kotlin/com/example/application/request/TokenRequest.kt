package com.example.application.request

import kotlinx.serialization.Serializable
import com.example.domain.entity.FirebaseToken
import org.bson.types.ObjectId
import io.ktor.server.routing.*

@Serializable // Anotación para la serialización
data class TokenRequest(
    val idToken: String // Solo recibe el idToken desde el cliente
)

fun TokenRequest.toDomain(userId: String): FirebaseToken {
    return FirebaseToken(
        id = ObjectId(), // Generar un nuevo ObjectId para la entidad
        userId = userId, // Asigna el userId extraído después de verificar el token
        token = idToken
    )
}
