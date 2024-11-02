// Routing.kt
package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import com.example.routes.authRoutes
import com.example.routes.firebaseTokenRoutes
import com.example.domain.ports.FirebaseTokenRepository // Asegúrate de importar el repositorio

fun Application.configureRouting(firebaseTokenRepository: FirebaseTokenRepository) {
    routing {
        authRoutes()
        firebaseTokenRoutes(firebaseTokenRepository) // Pasar el repositorio aquí
    }
}
