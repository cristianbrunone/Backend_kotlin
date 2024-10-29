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
        routing {
    authenticate("auth-jwt") {
        get("/protected") {
            call.respondText("Access granted to protected resource")
        }
    }
}

        authRoutes()
    }
}
