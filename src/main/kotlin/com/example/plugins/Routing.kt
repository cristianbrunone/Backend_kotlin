package com.example.plugins
import com.example.auth.authRoutes
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        // Ruta protegida que requiere autenticación
        authenticate("auth-jwt") {
            get("/protected") {
                val userId = call.principal<UserIdPrincipal>()?.name // Obtener el ID del usuario autenticado
                call.respondText("Hello, $userId! You have access to this protected route.")
            }
        }
          authRoutes()
       
    }
}
