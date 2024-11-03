package com.example.routes

import com.example.domain.entity.FirebaseToken
import com.example.domain.ports.FirebaseTokenRepository
import com.example.application.usecases.VerifyUserRegistrationUseCase
import com.example.application.request.TokenRequest
import com.example.application.request.toDomain
import com.example.application.services.TokenVerifier
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.*
import org.bson.types.ObjectId

fun Route.firebaseTokenRoutes(repository: FirebaseTokenRepository, verifyUserRegistrationUseCase: VerifyUserRegistrationUseCase) {
    route("/secure") {
    post("/firebase-token") {
        val idToken = call.request.headers["Authorization"]?.replace("Bearer ", "")
        if (idToken == null) {
            call.respond(HttpStatusCode.BadRequest, "Falta el token de autorización")
            return@post
        }

        try {
            // Verificar el token de Firebase
            val firebaseToken = TokenVerifier.verify(idToken)
            if (firebaseToken != null) {
                val userId = firebaseToken.uid
                val existingToken = repository.findByUserId(userId)

                if (existingToken != null) {
                    // Si el token ya está guardado, responder con 200 y continuar
                    call.application.environment.log.info("Token ya existente para UID: $userId, se continúa sin insertar")
                    call.respond(HttpStatusCode.OK, "Token ya existe, navegación permitida")
                } else {
                    // Si el token no existe, verificar el registro
                    val verificationResponse = verifyUserRegistrationUseCase.execute(userId)

                    if (verificationResponse.success) {
                        call.respondText("Usuario autenticado con UID: $userId, navegación permitida", status = HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.Conflict, "Usuario debe completar el registro en ${verificationResponse.redirectUrl ?: "/complete-registration"}")
                    }
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Token inválido o expirado")
            }
        } catch (e: Exception) {
            call.application.environment.log.error("Error al procesar el token: ${e.message}", e)
            call.respond(HttpStatusCode.InternalServerError, "Error interno del servidor")
        }
    }




        delete("/{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respondText(
                text = "Missing Firebase token id",
                status = HttpStatusCode.BadRequest
            )
            try {
                val deleteCount: Long = repository.deleteById(id)  // ID como String
                if (deleteCount == 1L) {
                    call.respondText("Firebase Token deleted successfully", status = HttpStatusCode.OK)
                } else {
                    call.respondText("Firebase Token not found", status = HttpStatusCode.NotFound)
                }
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
            }
        }

        get("/{id?}") {
            val id = call.parameters["id"]
            if (id.isNullOrEmpty()) {
                return@get call.respondText(
                    text = "Missing id",
                    status = HttpStatusCode.BadRequest
                )
            }
            try {
                val token = repository.findById(id)  // ID como String
                if (token != null) {
                    call.respond(token)
                } else {
                    call.respondText("No records found for id $id", status = HttpStatusCode.NotFound)
                }
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
            }
        }

        patch("/{id?}") {
            val id = call.parameters["id"] ?: return@patch call.respondText(
                text = "Missing Firebase token id",
                status = HttpStatusCode.BadRequest
            )
            try {
                val updatedToken = call.receive<FirebaseToken>()
                val updatedCount = repository.updateOne(id, updatedToken)  // ID como String
                call.respondText(
                    text = if (updatedCount == 1L) "Firebase Token updated successfully" else "Firebase Token not found",
                    status = if (updatedCount == 1L) HttpStatusCode.OK else HttpStatusCode.NotFound
                )
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
            }
        }
    }
}
