package com.example.auth

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import com.example.utils.TokenVerifier

fun Route.authRoutes() {
    post("/auth/login") {
        val idToken = call.receive<String>()

        // Verifica el token de Firebase
        val firebaseToken = TokenVerifier.verifyFirebaseToken(idToken)

        if (firebaseToken != null) {
            call.respondText("User authenticated with UID: ${firebaseToken.uid}", status = HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Invalid token")
        }
    }
}
