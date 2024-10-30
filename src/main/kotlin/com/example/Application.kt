package com.example

import com.example.plugins.configureRouting
import com.example.plugins.FirebaseConfig
import com.example.utils.FirebaseAuthService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.websocket.*
import io.ktor.server.plugins.cors.routing.* // Cambiado
import io.ktor.http.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    FirebaseConfig.initialize()
    
     install(CORS) {
        allowHost("192.168.100.53:8080") // Permitir tu IP local
        allowMethod(HttpMethod.Post)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
    }

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
