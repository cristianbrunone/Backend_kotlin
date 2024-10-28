package com.example.plugins

import com.example.auth.authRoutes
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

val logger = LoggerFactory.getLogger("Routing")

fun Application.configureRouting() {
    routing {
        // Ruta protegida que requiere autenticación
        authenticate("auth-jwt") {
            get("/protected") {
                val token = call.request.headers["Authorization"]?.removePrefix("Bearer ") ?: ""
                logger.info("Received token: $token (length: ${token.length})")
                val userId = call.principal<UserIdPrincipal>()?.name
                call.respondText("Hello, $userId! You have access to this protected route.")
            }
        }
        authRoutes()
    }
}
