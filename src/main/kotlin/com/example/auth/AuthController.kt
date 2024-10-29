// AuthController.kt
package com.example.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.utils.TokenVerifier

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
                call.respondText("Usuario autenticado con UID: ${firebaseToken.uid}")
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Token inválido o expirado")
            }
        }
    }
}
