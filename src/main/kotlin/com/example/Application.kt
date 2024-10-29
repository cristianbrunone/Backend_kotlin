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
            // Obtén el ID token directamente desde la carga útil del token
            val idToken = credential.payload.getClaim("sub").asString() // 'sub' es el UID en el token de Firebase

            // Verifica el token usando tu servicio
            val firebaseToken = FirebaseAuthService.verifyToken(idToken)
            if (firebaseToken != null) {
                UserIdPrincipal(firebaseToken.uid) // Retorna el principal del usuario
            } else {
                null // Token inválido
            }
        }
    }
}

    configureRouting() // Configura las rutas
}
