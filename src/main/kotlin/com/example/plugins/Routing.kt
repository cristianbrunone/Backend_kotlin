// Routing.kt
package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import com.example.routes.authRoutes
import com.example.routes.firebaseTokenRoutes
import com.example.domain.ports.FirebaseTokenRepository 
import com.example.application.usecases.VerifyUserRegistrationUseCase 


fun Application.configureRouting(firebaseTokenRepository: FirebaseTokenRepository, verifyUserRegistrationUseCase: VerifyUserRegistrationUseCase) {
    routing {
        authRoutes()
        firebaseTokenRoutes(firebaseTokenRepository, verifyUserRegistrationUseCase) 
    }
}
