// Routing.kt
package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import com.example.auth.authRoutes // Asegúrate de importar correctamente

fun Application.configureRouting() {
    routing {
        authRoutes() // Esto llama a la función de rutas de autenticación
    }
}
