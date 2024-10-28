package com.example

import com.example.plugins.configureRouting
import com.example.plugins.FirebaseConfig
import com.example.utils.FirebaseAuthService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.websocket.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    FirebaseConfig.initialize() // Inicializa Firebase

    install(WebSockets) // Instala WebSockets

    install(Authentication) {
        jwt("auth-jwt") {
            skipWhen { true } // Salta la verificación de JWT de Ktor
    
            validate { credential ->
                // Obtén el ID token directamente del claim
                val idToken = credential.payload.getClaim("user_id").asString()
                // Verifica el token usando tu servicio
                val firebaseToken = FirebaseAuthService.verifyToken(idToken)
                if (firebaseToken != null) {
                    UserIdPrincipal(firebaseToken.uid)
                } else {
                    null
                }
            }
        }
    }
    
    configureRouting() // Configura las rutas
}
