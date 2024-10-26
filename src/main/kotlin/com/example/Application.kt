package com.example

import com.example.plugins.configureRouting
import com.example.plugins.FirebaseConfig
import com.example.utils.TokenVerifier
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

    // Configuración de JWT
    install(Authentication) {
        jwt("auth-jwt") {
           verifier(TokenVerifier.verifier) // Usa el verificador de token definido en utils
            validate { credential ->
                // Asegúrate de que esto sea el tipo correcto
                val userId = credential.payload.getClaim("sub").asString()
                if (userId != null) {
                    UserIdPrincipal(userId)
                } else {
                    null
                }
            }
        }
    }

    configureRouting() // Configura las rutas
}
