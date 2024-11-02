// AuthController.kt
package com.example.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.application.services.TokenVerifier
import com.example.application.request.TokenRequest
import com.example.application.request.toDomain
import com.example.domain.entity.FirebaseToken

fun Route.authRoutes() {
    route("/secure") {
        post("/authenticate") {
            val idToken = call.request.headers["Authorization"]?.replace("Bearer ", "")
            if (idToken == null) {
                call.respond(HttpStatusCode.BadRequest, "Falta el token de autorización")
                return@post
            }
            val firebaseToken = TokenVerifier.verify(idToken)
            if (firebaseToken != null) {
                // Crear una instancia de TokenRequest
                val tokenRequest = TokenRequest(idToken = idToken)

                // Convertir TokenRequest a FirebaseToken usando el método toDomain
                val domainToken = tokenRequest.toDomain(firebaseToken.uid)

                // Responder con detalles del token (puedes cambiar esto según lo necesites)
                call.respondText("Usuario autenticado con UID: ${domainToken.userId}")
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Token inválido o expirado")
            }
        }
    }
}
